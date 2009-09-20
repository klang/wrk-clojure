;; make the good old hello world application
;; make a jar file based on the clojure code
;; use the jar file in a java/tomcat context

(def hello (fn [] "Hello world"))


(defn argcount
  ([] 0)
  ([x] 1)
  ([x y] 2)
  ([x y & more] (+ (argcount x y) (count more))))

;; make-adder :: (int) -> (int -> int)
;; x is bound to y and a closeure is returned 
(defn make-adder [x]
  (let [y x]
    (fn [z] (+ y z))))

(def add2 (make-adder 2))

(add2 4)

(defn make-sequence [x]
  (let [init (or x 0)]
    (fn [z] (+ 1 init))))

(def seqA 10)
; (seqA)
; =>10
; (seqA)
; =>11
; ...
; http://blog.morrisjohns.com/javascript_closures_for_dummies
; function say667() {
;   // Local variable that ends up within closure
;   var num = 666;
;   var sayAlert = function() { alert(num); }
;   num++;
;   return sayAlert;
; }
(defn say667[]
  (let [num 666]
    (fn [num] (num))
    (+ 1 num))
  )
; function setupSomeGlobals() {
;   // Local variable that ends up within closure
;   var num = 666;
;   // Store some references to functions as global variables
;   gAlertNumber = function() { alert(num); }
;   gIncreaseNumber = function() { num++; }
;   gSetNumber = function(x) { num = x; }
; }
(defn setupSomeGlobals []
  (let [num 666]
))

(defn make-countdown [x]
  (let [y x]
    (fn [] ())))

(def count10 (make-countdown 10))
(count10)
=>10
(count10)
=>9
(count10)
=>8
...
(count10)
=>1
(count10)
=>0
(count10)
=>0

; http://www.psg.com/~dlamkins/sl/chapter11.html#closures
; http://www.psg.com/~dlamkins/sl/chapter15.html
(let [counter 0]
  (defn counter-next []
    (def inc (inc counter)))
  (defn counter-reset []
    (def counter 0)))

 (counter-next)