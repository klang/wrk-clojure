;; Creating Vars
;; source, show
;; (use 'clojure.contrib.repl-utils)

;; (create-struct & key-symbols)

(def person (create-struct :first-name :last-name))

;; (defstruct name & key-symbols)

(source defstruct)
(defmacro defstruct
  "Same as (def name (create-struct keys...))"
  [name & keys]
  `(def ~name (create-struct ~@keys)))

;; (declare & names)
(source declare)
(defmacro declare
  "defs the supplied var names with no bindings, useful for making forward declarations."
  [& names] `(do ~@(map #(list 'def %) names)))

(declare a b c)

(#(list 'def %) 'a)
(map #(list 'def %) '[a b c d])
`(do ~@(map #(list 'def %) '[a b c d]))
(macroexpand-1 '(declare a b c d))
