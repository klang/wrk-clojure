(defn unless [expr form]
  (if expr nil form))

(unless false (println "this should print"))
(unless true (println "this should not print"))

(defn unless [expr form]
  (println "About to test ...")
  (if expr nil form))

(unless false (println "this should print"))
(unless true (println "this should not print"))

(defmacro unless [expr form]
  (list 'if expr nil form))

(unless false (println "this should print"))
(unless true (println "this should not print"))

(macroexpand-1 '(unless false (println "this should print")))

(defmacro bad-unless [expr form]
  (list 'if 'expr nil form))

(macroexpand-1 '(bad-unless false (println "this should print")))

(defmacro bad-unless false (println "this should print"))

(macroexpand-1 '(.. arm getHand getFinger))
(macroexpand '(.. arm getHand getFinger))

(macroexpand '(and 1 2 3))

(unless false (println "this") (println " and also this"))

(defmacro chain [x form]
  (list `. x form))

(defmacro chain
  ([x form] (list '. x form))
  ([x form & more] (concat (list 'chain (list '. x form)) more)))
(defmacro chain
  ([x form] (list `. x form))
  ([x form & more] (concat (list `chain (list `. x form)) more)))
;; the two versions of chain does the same .. use of ` and ' is equivalent?
;; -- in this case yes
(macroexpand '(chain arm getHand))
(macroexpand '(chain arm getHand getFinger))

(defmacro chain [x form] 
  `(. ~x ~form))

(macroexpand '(chain arm getHand))
(macroexpand '(chain arm getHand getFinger))

;; Does not quite work

(defmacro chain
  ([x form] `(. ~x ~form))
  ([x form & more] `(chain (. ~x ~form) ~more)))

(macroexpand '(chain arm getHand))
(macroexpand '(chain arm getHand getFinger))


(defmacro chain
  ([x form] `(. ~x ~form))
  ([x form & more]  `(chain (. ~x ~form) ~@more)))
(defmacro chain
  ([x form] '(. ~x ~form))
  ([x form & more]  '(chain (. ~x ~form) ~@more)))

;; the two versions of chain are NOT the same ` and ' are not the same

(macroexpand '(chain arm getHand))
(macroexpand '(chain arm getHand getFinger))
