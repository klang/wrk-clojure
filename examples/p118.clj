;; Functions on Sets
;; examples/sequences.clj

(use 'clojure.set)
(def languages #{"java" "c" "d" "clojure"})
(def letters #{"a" "b" "c" "d" "e"})
(def beverages #{"java" "chai" "pop" })

;; union
;; intersection
;; difference
;; select

(union languages beverages)
(difference languages beverages)
(intersection languages beverages)

;; TODO: files modified on a branch in cvs

(select #(= 1 (.length %)) languages)

;; examples/sequences.clj
(def compositions 
  #{{:name "The Art of the Fugue" :composer "J. S. Bach"}
    {:name "Musical Offering" :composer "J. S. Bach"}
    {:name "Requiem" :composer "Giuseppe Verdi"}
    {:name "Requiem" :composer "W. A. Mozart"}})
(def composers
  #{{:composer "J. S. Bach" :country "Germany"}
    {:composer "W. A. Mozart" :country "Austria"}
    {:composer "Giuseppe Verdi" :country "Italy"}})
(def nations
  #{{:nation "Germany" :language "German"}
    {:nation "Austria" :language "German"}
    {:nation "Italy" :language "Italian"}})

;; (rename relation rename-map)
(rename compositions {:name :title})

;; (select pred relation)
;; SQL; ... FROM compositions WHERE :name = "Requiem"
(select #(= (:name %) "Requiem") compositions)

;; (project relation keys)
;; SQL: SELECT :name FROM compositions
(project compositions [:name])

;; (join relation-1 relation-2 keymap?)
(join compositions composers)

(join composers nations {:country :nation})

(project
 (join
  (select #(= (:name %) "Requiem") compositions)
  composers)
 [:country])