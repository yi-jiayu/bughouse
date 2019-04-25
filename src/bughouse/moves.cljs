(ns bughouse.moves)

(defn vadd
  "Element-wise addition of two vectors"
  [v1 v2]
  (mapv + v1 v2))

(defn valid-horse-moves
  [board location]
  (let [enemy-colour (if (= "r" (first (get-in board location))) "b" "r")]
    (into #{} (remove nil? (map (fn [[adj target]]
                                  (when (and (= "" (get-in board adj)) ; adjacent square is empty
                                             (let [target-square (get-in board target)]
                                               ; target square is empty or contains an enemy piece
                                               (or (= "" target-square)
                                                   (= enemy-colour (first target-square)))))
                                    target))
                                [[(vadd [-1 0] location) (vadd [-2 -1] location)] ; all 8 possible horse moves
                                 [(vadd [-1 0] location) (vadd [-2 1] location)]
                                 [(vadd [0 -1] location) (vadd [-1 -2] location)]
                                 [(vadd [0 -1] location) (vadd [1 -2] location)]
                                 [(vadd [0 1] location) (vadd [-1 2] location)]
                                 [(vadd [0 1] location) (vadd [1 2] location)]
                                 [(vadd [1 0] location) (vadd [2 -1] location)]
                                 [(vadd [1 0] location) (vadd [2 1] location)]])))))

(defn valid-moves
  "Returns the valid moves for a piece on a board."
  [board location]
  (case (second (get-in board location))
    "H" (valid-horse-moves board location)
    #{}))
