(defn create-runonce [function]
  (let [sentinel (Object.)
	result (atom sentinel)]
    (fn [& args]
      (locking sentinel
	(if (= @result sentinel)
	  (reset! result (function))
	  @result)))))

(def println-once (create-runonce println))

(println-once "there can be only one!")
(println-once "there can be only one!")

;; where is the output?

(println "here")
(println "there?")