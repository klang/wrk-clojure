;; Working with Java Callback APIs

;; (set! var-symbol new-value)

;; redacted from Clojure's xml.clj to focus on dynamic variable usage
(startElement
 [uri local-name q-noame #^Attributes atts]
 ;details omitted
 (set! *stack* (conj *stack* *current*))
 (set! *current* e)
 (set! *state* :element))
nil)
