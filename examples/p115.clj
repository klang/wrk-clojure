;; Functions on Maps

;;(keys map)
;;(vals map)
(keys {:sundance "spaniel" :darwin "beagle"})
(vals {:sundance "spaniel" :darwin "beagle"})

;;(get map key value-if-not-found?)

(get {:sundance "spaniel" :darwin "beagle"} :darwin)
(get {:sundance "spaniel" :darwin "beagle"} :snoopy)

;; or using the map as a function
({:sundance "spaniel" :darwin "beagle"} :darwin)
({:sundance "spaniel" :darwin "beagle"} :snoopy)

;; or using the keyword as a function
(:darwin {:sundance "spaniel" :darwin "beagle"})
(:snoopy {:sundance "spaniel" :darwin "beagle"})

;;(contains? map key)
(contains? {:sundance "spaniel" :darwin "beagle"} :darwin)
(contains? {:sundance "spaniel" :darwin "beagle"} :snoopy)

(def score {:stu nil :joey 100})
(:stu score)
(contains? score :stu)
(get score :stu :score-not-found)
(get score :aaron :score-not-found)

;; building maps
;; (doc assoc)
;; (doc dissoc)
;; (doc select-keys)
;; (doc merge)

;; examples/sequences.clj

(def song {:name "Agnus Drei"
	   :artist "Krysztof Penderecki"
	   :album "Polish Requiem"
	   :genre "Classical"})

(assoc song :kind "MPEG Audio File")
(dissoc song :genre)
(select-keys song [:name :artist])
(merge song {:size 8818166 :time 507245})

;; (merge-with merge-fn & maps)

(merge-with
 concat
 {:rubble ["Barney"] :flintstone ["Fred"]}
 {:rubble ["Betty"] :flintstone ["Wilma"]}
 {:rubble ["Bam-Bam"] :flintstone ["Pebbles"]}
)
