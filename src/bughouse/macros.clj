(ns bughouse.macros)

(defmacro enemy?
  [piece]
  (list '= '(case (first (get-in board location))
                 "r" "b"
                 "b" "r") (list 'first piece)))
