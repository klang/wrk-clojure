;; Java Interop

;; source, show
;; (use 'clojure.contrib.repl-utils)

;; user> Math/PI
;; 3.141592653589793
;; user> (Math/pow 10 3)
;; 1000.0
;; user> (def PI Math/PI)
;; #'user/PI
;; user> PI
;; 3.141592653589793
;; user> (defn pow [b e] (Math/pow b e))
;; #'user/pow
;; user> (pow 10 3)
;; 1000.0
;; user>

(use '[clojure.contrib.import-static :only (import-static)])
(import-static java.lang.Math PI pow)

;; Postponing Evaluation

;;(delay & vars)
;;(source delay)
(defmacro delay
  "Takes a body of expressions and yields a Delay object that will
  invoke the body only the first time it is forced (with force), and
  will cache the result and return it on all subsequent force
  calls."  
  [& body]
    (list 'new 'clojure.lang.Delay (list* `#^{:once true} fn* [] body)))

(def slow-calc (delay (Thread/sleep 5000) "done!"))

;; user> (force slow-calc)
;; (5 seconds passes)
;; "done!"
;; user> (force slow-calc)
;; (result is printed instantly)
;; "done!"

;; Wrapping Evaluation
;; (with-out-str & exprs)
;; (source with-out-str)
(defmacro with-out-str
  "Evaluates exprs in a context in which *out* is bound to a fresh
  StringWriter.  Returns the string created by any nested printing
  calls."
  [& body]
  `(let [s# (new java.io.StringWriter)]
     (binding [*out* s#]
       ~@body
       (str s#))))

(with-out-str (print "hello, ") (print "world"))

;; (assert expr)
;; (source assert)
(defmacro assert
  "Evaluates expr and throws an exception if it does not evaluate to
 logical true."
  [x]
  `(when-not ~x
     (throw (new Exception (str "Assert failed: " (pr-str '~x))))))

;; user> (assert (= 1 1))
;; nil
;; user> (assert (= 1 2))
;; ; Evaluation aborted.

;; Avoiding Lambdas

(defn bench-fn [f]
  (let [start (System/nanoTime)
	result (f)]
    {:result result :elapsed (- (System/nanoTime) start)}))

;; macro
(bench (+ 1 2))

;; function
(bench-fn (fn []> (+ 1 2)))

