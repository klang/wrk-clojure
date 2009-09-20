; (defun make-cd (title artist rating ripped)
;  (list :title title :artist artist :rating rating :ripped ripped))

(defstruct cd :title :artist :rating :ripped)
(defn add-records [db & cd] (into db cd))

; (defvar *db* nil)
; (defun add-record (cd) (push cd *db*))

(defn init-db []
  (add-records #{} 
	       (struct cd "Roses" "Kathy Mattea" 7 true)
	       (struct cd "Fly" "Dixie Chicks" 8 true)
	       (struct cd "Home" "Dixie Chicks" 9 true)))

; (defun dump-db ()
;   (dolist (cd *db*)
;     (format t "~{~a:~10t~a~%~}~%" cd)))

(defn dump-db [db]
  (doseq [cd db]
    (doseq [[key value] cd]
      (print (format "%10s: %s\n" (name key) value)))
    (println)))

; (defun prompt-read (prompt)
;   (format *query-io* "~a: " prompt)
;   (force-output *query-io*)
;   (read-line *query-io*))

(defn prompt-read [prompt]
  (print (format "%s: " prompt))
  (flush)
  (read-line))

; (defun prompt-for-cd ()
;   (make-cd
;    (prompt-read "Title")
;    (prompt-read "Artist")
;    (or (parse-integer (prompt-read "Rating") :junk-allowed t) 0)
;    (y-or-n-p "Ripped [y/n]: ")))

(defn parse-integer [str]
  (try (Integer/parseInt str) 
       (catch NumberFormatException nfe 0)))

(defn y-or-n-p [prompt]
  (= "y"
     (loop []
       (or 
        (re-matches #"[yn]" (.toLowerCase (prompt-read prompt)))
        (recur)))))

(defn prompt-for-cd []
  (struct 
   cd 
   (prompt-read "Title")
   (prompt-read "Artist")
   (parse-integer (prompt-read "Rating"))
   (y-or-n-p "Ripped [y/n]")))


;(defun add-cds ()
;  (loop (add-record (prompt-for-cd))
;      (if (not (y-or-n-p "Another? [y/n]: ")) (return))))

; (defun save-db (filename)
;   (with-open-file (out filename
;                    :direction :output
;                    :if-exists :supersede)
;     (with-standard-io-syntax
;       (print *db* out)))
 
(use 'clojure.contrib.duck-streams)
(defn save-db [db filename]
  (spit 
   filename 
   (with-out-str (print db))))

; (defun load-db (filename)
;   (with-open-file (in filename)
;     (with-standard-io-syntax
;       (setf *db* (read in)))))

(defn load-db [filename] 
  (with-in-str (slurp filename)
    (read)))

(defn artist-selector [artist]
  #(= (:artist %) artist))

(defn where [criteria]
  (fn [m]
    (loop [criteria criteria] 
      (let [[k,v] (first criteria)]
        (or (not k)
            (and (= (k m) v) (recur (rest criteria))))))))

(defn simpler-where [criteria]
  (fn [m]
    (every? (fn [[k v]] (= (k m) v)) criteria)))