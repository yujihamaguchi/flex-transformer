(ns flex-transformer.core
  (:import (java.io File FileInputStream FileOutputStream
                    InputStream InputStreamReader
                    OutputStream OutputStreamWriter
                    BufferedReader BufferedWriter
                    SequenceInputStream)
           (clojure.lang SeqEnumeration)
          　(java.net URL))
  (:gen-class))

(defprotocol IOFactory
  (make-reader [this])
  (make-writer [this]))

(defn is-connectable-url? [url]
  (let [response-code (->> url .openConnection .getResponseCode)]
    (if (= 200 response-code)
      true
      (throw (IllegalArgumentException. (str "Can't connect to (" url ") - Response Code " response-code "."))))))

(extend-type InputStream
  IOFactory
  (make-reader [src]
    (-> src InputStreamReader. BufferedReader.))
  (make-writer [dst]
    (throw (IllegalArgumentException. "Can't open as InputStream."))))

(extend-type OutputStream
  IOFactory
  (make-reader [src]
    (throw (IllegalArgumentException. "Can't open as OutputStream.")))
  (make-writer [dst]
    (-> dst OutputStreamWriter. BufferedWriter.)))

(extend-type URL
  IOFactory
  (make-reader [src]
    (make-reader
      (if (is-connectable-url? src)
        (if (= "file" (.getProtocol src))
          (-> src .getPath FileInputStream.)
          (.openStream src)))))
  (make-writer [dst]
    (make-writer
      (if (= "file" (.getProtocol dst))
        (-> dst .getPath FileInputStream.)
        (throw (IllegalArgumentException. "Can't write to URL"))))))

(extend-type File
  IOFactory
  (make-reader [src]
    (make-reader (FileInputStream. src)))
  (make-writer [dst]
    (make-writer (FileOutputStream. dst))))

; support multiple URLs
(deftype URLs [urls])
(extend-type URLs
  IOFactory
  (make-reader [src]
    (if (every? is-connectable-url? (map #(URL. %) (.urls src)))
      (make-reader
        (->> src
             .urls
             (map 
               (comp
                 #(if (= "file" (.getProtocol %))
                   (-> % .getPath FileInputStream.)
                   (.openStream %))
                 #(URL. %)))
             SeqEnumeration.
             SequenceInputStream.))))
  (make-writer [dst]
    (throw (IllegalArgumentException. "Can't write to URLs."))))

(defn input [src]
  (let [sb (StringBuilder.)]
    (with-open [reader (make-reader src)]
      (loop [c (.read reader)]
        (if (neg? c)
          (str sb)
          (do
            (.append sb (char c))
            (recur (.read reader))))))))

(defn transform [match replacement src]
  (clojure.string/replace src match replacement))

(defn output [content & dsts]
  (dorun (map
    #(with-open [writer (make-writer %)]
      (.write writer (str content)))
    dsts)))

(defn -main [match replacement & srcs]
  (try
    (if (> 1 (count srcs))
      (throw (IllegalArgumentException. "Wrong number of args."))
      (output
        (->> srcs
             URLs.
             input
             ((partial transform match replacement)))
         System/out
         (File. "./output.log"))) ; also output to file
    (catch Exception e
      (binding [*out* *err*]
        (println e)))))