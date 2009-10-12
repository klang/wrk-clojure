;; Conditional Evaluation
;; source, show
;; (use 'clojure.contrib.repl-utils)

(source and)
(defmacro and
  "Evaluates exprs one at a time, from left to right. If a form
  returns logical false (nil or false), and returns that value and
  doesn't evaluate any of the other expressions, otherwise it returns
  the value of the last expr. (and) returns true."
  ([] true)
  ([x] x)
  ([x & next]
   `(let [and# ~x]
      (if and# (and ~@next) and#))))

(and 1 0 nil false)
;;=> nil
(and 1 0 false nil)
;;=> false

(source comment)

(defmacro comment
  "Ignores body, yields nil"
  [& body])

(comment
(println "hello world")
)

