;;__________________________________________________________________________
;;;; 
;;;;

;;(if (include 'slime) (slime-setup))
;;__________________________________________________________________________
;;;;    Programming - Clojure

(defvar clj-root (concat (expand-file-name "~") "/lisp/clj/"))
(setq default-directory clj-root)
(add-to-list 'exec-path (concat clj-root "bin"))

(setq load-path (append (list (concat clj-root "slime")
			      (concat clj-root "slime/contrib")
			      (concat clj-root "clojure-mode")
			      (concat clj-root "swank-clojure"))
			load-path))

;; (defun cygpath (path)
;;   (if (not running-on-mswindows-p)
;;       path
;;     (substring (shell-command-to-string (concat "cygpath -wp " path)) 0 -1)
;;     ))
;; (cygpath "~/lisp/clj/clojure/trunk/clojure.jar:clojure-contrib/trunk/clojure-contrib.jar")

(setq swank-clojure-binary (concat clj-root "bin/clj-cmd"))
(defvar clj-cmd)
(setenv "CLJ_CMD"
	(setq clj-cmd
	      (concat "java "
		      "-server "
		      "-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8888 "
		      "-cp "
		      ;; windows want the classpath in a slightly different way
		      ;; cygpath could be used if expand-file-name was not
		      (concat "'" (concat clj-root "clojure/trunk/clojure.jar") ";"
		                 ;(concat (expand-file-name "~") "/.clojure")  ";"
		                 ;(concat clj-root "src/book-code")  ";"
		                  (concat clj-root "clojure-contrib/trunk/clojure-contrib.jar") "'")
		      " clojure.lang.Repl")))
(setenv "CLJ_CMD" "java -server -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=8888 -cp 'c:/klang/lisp/clj/clojure/trunk/clojure.jar;c:/klang/lisp/clj/clojure-contrib/trunk/clojure-contrib.jar' clojure.lang.Repl")

(eval-after-load "slime"
  '(progn
     (slime-setup)
     (setq slime-lisp-implementations
	   `((clojure ("c:/klang/lisp/clj/bin/clj-cmd") :init swank-clojure-init)
	     ,@slime-lisp-implementations))))

(require 'clojure-auto)
(require 'swank-clojure-autoload)

(defun slime-java-describe (symbol-name)
  "Get details on Java class/instance at point."
  (interactive (list (slime-read-symbol-name "Java Class/instance: ")))
  (when (not symbol-name)
    (error "No symbol given"))
  (save-excursion
    (set-buffer (slime-output-buffer))
    (unless (eq (current-buffer) (window-buffer))
      (pop-to-buffer (current-buffer) t))
    (goto-char (point-max))
    (insert (concat "(show " symbol-name ")"))
    (when symbol-name
      (slime-repl-return)
      (other-window 1))))

(defun slime-javadoc (symbol-name)
  "Get JavaDoc documentation on Java class at point."
  (interactive (list (slime-read-symbol-name "JavaDoc info for: ")))
  (when (not symbol-name)
    (error "No symbol given"))
  (set-buffer (slime-output-buffer))
  (unless (eq (current-buffer) (window-buffer))
    (pop-to-buffer (current-buffer) t))
  (goto-char (point-max))
  (insert (concat "(clojure.contrib.javadoc/javadoc " symbol-name ")"))
  (when symbol-name
    (slime-repl-return)
    (other-window 1)))

(setq slime-browse-local-javadoc-root (concat clj-root "java"))

(defun slime-browse-local-javadoc (ci-name)
  "Browse local JavaDoc documentation on Java class/Interface at point."
  (interactive (list (slime-read-symbol-name "Class/Interface name: ")))
  (when (not ci-name)
    (error "No name given"))
  (let ((name (replace-regexp-in-string "\\$" "." ci-name))
	(path (concat (expand-file-name slime-browse-local-javadoc-root) "/docs/api/")))
    (with-temp-buffer
      (insert-file-contents (concat path "allclasses-noframe.html"))
      (let ((l (delq nil
		     (mapcar #'(lambda (rgx)
				 (let* ((r (concat "\\.?\\(" rgx "[^./]+\\)[^.]*\\.?$"))
					(n (if (string-match r name)
					       (match-string 1 name)
					     name)))
				   (if (re-search-forward (concat "<A HREF=\"\\(.+\\)\" +.*>" n "<.*/A>") nil t)
				       (match-string 1)
				     nil)))
			     '("[^.]+\\." "")))))
	(if l
	    (browse-url (concat "file://" path (car l)))
	  (error (concat "Not found: " ci-name)))))))

(defun run-clojure ()
  "Starts clojure in Slime"
  (interactive)
  (slime 'clojure))

(global-set-key [f5] 'run-clojure)
(global-set-key [(control f11)] 'slime-selector)

(add-hook 'slime-connected-hook (lambda ()
				  (interactive)
				  (slime-redirect-inferior-output)
				  (define-key slime-mode-map (kbd "<return>") 'newline-and-indent)
				  (define-key slime-mode-map (kbd "C-j") 'newline)
				  (define-key slime-mode-map (kbd "C-c b") 'slime-browse-local-javadoc)
				  (define-key slime-repl-mode-map (kbd "C-c b") 'slime-browse-local-javadoc)
				  (define-key slime-mode-map (kbd "C-c d") 'slime-java-describe)
				  (define-key slime-repl-mode-map (kbd "C-c d") 'slime-java-describe)
				  (define-key slime-mode-map (kbd "C-c D") 'slime-javadoc)
				  (define-key slime-repl-mode-map (kbd "C-c D") 'slime-javadoc)))
