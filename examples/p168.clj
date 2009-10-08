;; Validating Agents and Handling Errors

;; (agent initial-state options*)
;; options include:
;;  :validator validate-fn
;;  :meta metadata-map

(def counter (agent 0 :validator number?))

(send counter (fn [_] "boo"))

@counter

(agent-errors counter)
(clear-agent-errors counter)

@counter
