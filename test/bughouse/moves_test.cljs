(ns bughouse.moves-test
  (:require
    [cljs.test :refer-macros [deftest is testing]]
    [bughouse.core :refer [starting-position]]
    [bughouse.moves :refer [valid-horse-moves valid-moves]]))

(deftest valid-horse-moves-test
  (testing "all valid horse moves"
    (let [board [["" "" "" "" ""]
                 ["" "" "" "" ""]
                 ["" "" "rH" "" ""]
                 ["" "" "" "" ""]
                 ["" "" "" "" ""]]
          location [2 2]]
      (is (= #{[0 1] [0 3] [1 0] [3 0] [1 4] [3 4] [4 1] [4 3]} (valid-horse-moves board location)))))
  (testing "cannot move off board"
    (let [board [["" "" ""]
                 ["" "" ""]
                 ["" "" "rH"]]
          location [2 2]]
      (is (= #{[0 1] [1 0]} (valid-horse-moves board location)))))
  (testing "horse moves when blocked"
    (let [board [["" "" "" "" ""]
                 ["" "" "rP" "" ""]
                 ["" "rP" "rH" "" ""]
                 ["" "" "" "" ""]
                 ["" "" "" "" ""]]
          location [2 2]]
      (is (= #{[1 4] [3 4] [4 1] [4 3]} (valid-horse-moves board location)))))
  (testing "can only capture enemy pieces"
    (let [board [["" "rP" "" "rP" ""]
                 ["rP" "" "" "" "bP"]
                 ["" "" "rH" "" ""]
                 ["rP" "" "" "" "bP"]
                 ["" "bP" "" "bP" ""]]
          location [2 2]]
      (is (= #{[1 4] [3 4] [4 1] [4 3]} (valid-horse-moves board location))))))

(deftest valid-moves-test
  (testing "no piece at location"
    (is (= #{} (valid-moves starting-position [1 0]))))
  (testing "horse moves"
    (with-redefs [valid-horse-moves (fn [board location] [board location])]
      (is (= [starting-position [9 7]] (valid-moves starting-position [9 7]))))))
