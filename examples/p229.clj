;; Living without multimethods

(defn my-print [ob]
  (.write *out* ob))

(defn my-println [ob]
     (my-print ob)
     (.write *out* "\n"))
(my-println "hello")
(my-println nil)
(defn my-print [ob]
  (cond
    (nil? ob) (.write *out* "nil")
    (string? ob) (.write *out* ob)))
(my-println nil)
(my-println [1 2 3])
(use '[clojure.contrib.str-utils :only (str-join)])
(defn my-print-vector [ob]
  (.write *out* "[")
  (.write *out* (str-join " " ob))
  (.write *out* "]"))
(defn my-print [ob]
  (cond
    (vector? ob) (my-print-vector ob)
    (nil? ob) (.write *out* "nil")
    (string? ob) (.write *out* ob)))
(my-println [1 2 3])

;; Defining multimethods

;;(defmulti name dispatch-fn)
(defmulti my-print class)
(my-print "foo")

;;(defmethod name dispatch-val & fn-trail)

(defmethod my-print String [s]
  (.write *out* s))

(my-println "stu")

(defmethod my-print nil [s]
  (.write *out* "nil"))

(my-println nil)

(defmethod my-print Number [n]
  (.write *out* (.toString n)))

(my-println 42)
;; observe that 42 is an Integer, not a Number .. 

;;(isa? child parent)

(isa? Integer Number)
;; => true
(isa? Number Integer)
;; => false

(defmethod my-print :default [s]
  (.write *out* "#<")
  (.write *out* (.toString s))
  (.write *out* ">"))

(my-println (java.sql.Date. 0))
(my-println (java.util.Random.))

(defmulti name dispatch-fn :default default-value)

(defmulti my-print class :default :everything-else)
(defmethod my-print String [s]
  (.write *out* s))
(defmethod my-print :everything-else [_]
  (.write *out* "Not implemented yet.."))

;; Moving Beyond Simple Dispatch

(use '[clojure.contrib.str-utils :only (str-join)])
(defmethod my-print java.util.Collection [c]
  (.write *out* "(")
  (.write *out* (str-join " " c))
  (.write *out* ")"))
(my-println (take 6 (cycle [1 2 3])))
(my-println [1 2 3])

(defmethod my-print clojure.lang.IPersistentVector [c]
  (.write *out* "[")
  (.write *out* (str-join " " c))
  (.write *out* "]"))

(my-println [1 2 3])
;; Multiple methods in multimethod 'my-print' match dispatch value: class clojure.lang.LazilyPersistentVector -> interface java.util.Collection and interface clojure.lang.IPersistentVector, and neither is preferred
;;  [Thrown class java.lang.IllegalArgumentException]

;; (prefer-method multi-name loved-dispatch dissed-dispatch)

(prefer-method my-print clojure.lang.IPersistentVector java.util.Collection)

(my-println [1 2 3])

;; Creating Ad Hoc Taxonomies (practice & science of classification)

;; goto file examples/multimethods/account.clj

(System/getProperty "java.class.path")