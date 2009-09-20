(setq load-path (append (list "~/lisp/clj/slime"
                              "~/lisp/clj/slime/contrib"
                              "~/lisp/clj/clojure/clojure-mode"
                              "~/lisp/clj/clojure/swank-clojure")
                        load-path))

(setq swank-clojure-binary "clojure")

(require 'clojure-auto)
(require 'swank-clojure-autoload)

(defun run-clojure ()
  "Starts clojure in Slime"
  (interactive)
  (slime 'clojure))

(global-set-key [f5] 'run-clojure)
(global-set-key [(control f11)] 'slime-selector)

(add-hook 'slime-connected-hook 'slime-redirect-inferior-output) 