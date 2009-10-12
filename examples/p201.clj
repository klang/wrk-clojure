;; syntax quote, unquote and splicing unquote

;; source, show
;; (use 'clojure.contrib.repl-utils)

(time (str "a" "b"))

(souce time)
(defmacro time1
  "Evaluates expr and prints the time it took.  Returns the value of expr."
  [expr]
  `(let [start# (. System (nanoTime))
         ret# ~expr
	 end# (. System (nanoTime))]
     (prn (str "Elapsed time: " (/ (double (- end# start#)) 1000000.0) " msecs"))
     ret#))

(time1 (str "a" "b"))
(time (str "a" "b"))

;; (bench (str "a" "b"))
;; should expand to
(let [start (System/nanoTime)
      result (str "a" "b")]
  {:result result :elapsed (- (System/nanoTime) start)})

(defmacro bench [expr]
  `(let [start (System/nanoTime)
	 result ~expr]
     {:result result :elapsed (- (System/nanoTime) start)}))

(macroexpand-1 '(bench (str "a" "b")))

`foo#

(defmacro bench [expr]
  `(let [start# (System/nanoTime)
	 result# ~expr]
     {:result result# :elapsed (- (System/nanoTime) start#)}))

(macroexpand-1 '(bench (str "a" "b")))

(bench (str "a" "b"))




