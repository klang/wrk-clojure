(use '[clojure.xml :only (parse)])
(import '(java.io File))

(map #(.getName %) (.listFiles (File. "wrk-clojure/tutorials/")))

(defn book-startup [file]
  (str "wrk-clojure/tutorials/programming-clojure/" 
       (apply str (drop-while #{\/} file))))

(def file "/foo")
(if (= \/ (first file)) (apply str (rest file)) file)
(apply str (drop-while #{\/} file))

(map #(.getName %) (.listFiles (File. (book-startup "examples"))))

(parse (File. (book-startup "examples/sequences/compositions.xml")))

(for [x (xml-seq
	 (parse (File. (book-startup "examples/sequences/compositions.xml")))
      :when (= :composition (:tag x))]
  (:composer (:attrs x)))
