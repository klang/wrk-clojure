(defn make-counter [init-val] 
  (let [c (atom init-val)] #(swap! c inc)))
;; should 'commute' be used?
;; we don't care about the value, just that it should be one more


(def c (make-counter 0))

(c)

(defn make-another-counter [init-val] 
  (let [c (atom init-val)] 
    {:next #(swap! c inc)
     :reset #(reset! c init-val)}))

(def cc (make-another-counter 10))

((cc :next))
((cc :reset))

;-------------------

; // From Apache Commons Lang, http://commons.apache.org/lang/
; public static int indexOfAny(String str, char[] searchChars) {
;     if (isEmpty(str) || ArrayUtils.isEmpty(searchChars)) {
; 	return -1;
;     }
;     for (int i = 0; i < str.length(); i++) {
; 	char ch = str.charAt(i);
; 	for (int j = 0; j < searchChars.length; j++) {
; 	    if (searchChars[j] == ch) {
; 		return i;
; 	    }
; 	}
;     }
;     return -1;
; }

(defn indexed [coll] (map vector (iterate inc 0) coll))
(defn index-filter [pred coll]
  (when pred 
    (for [[idx elt] (indexed coll) :when (pred elt)] idx)))

(index-filter #{\a \e \i \o \u} "Lts f cnsnts nd n vwel")

