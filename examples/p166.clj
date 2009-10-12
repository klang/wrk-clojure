;; Use Atoms for Uncoordinated, Synchronous Updates

;; (atom initial-state options?)
;; (doc atom)
;; options
;;   :validator validate-fn
;;   :meta metadata-map


(def current-track (atom "Venus, the Bringer of Peace"))

(deref current-track)
@current-track

;; (reset! an-atom newval)

(reset! current-track "Credo")

;; coordinated update of both current-track and current-composer
;; is not possible when using atoms .. use refs instead

;; up to a point, at least .. 

(def current-track (atom {:title "Credo" :composer "Byrd"}))

(reset! current-track {:title "Spem in Alium" :composer "Tallis"})
@current-track

;; (Swap! an-atop f & args)

(swap! current-track assoc :title "Sancte Deus")

;; (agent initial-state)

(def counter (agent 0))

;; (send agent update-fn & args)

(send counter inc)
@counter

;; (await & agents)
;; (await-for timeout-millis & agents)


