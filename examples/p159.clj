;; Refs and Software Transactional Memory

;; (ref initial-state)
(def current-track (ref "Mars, the Bringer of Wars"))

;; (deref reference)
(deref current-track)
@current-track

;; (ref-set reference new-value)
(ref-set current-track "Venus, the Bringer of Peace")
;; No transaction running
;;  [Thrown class java.lang.IllegalStateException]

;; (dosync & exprs)
(dosync (ref-set current-track "Venus, the Bringer of Peace"))

(def current-track (ref "Venus, the Bringer of Peace"))
(def current-composer (ref "Holst"))

(dosync
 (ref-set current-track "Credo")
 (ref-set current-composer "Byrd"))