(ns bughouse.moves-test
  (:require
    [cljs.test :refer-macros [deftest is testing]]
    [bughouse.core :refer [starting-position]]
    [bughouse.moves :refer [valid-horse-moves valid-elephant-moves valid-moves]]))

(def empty-board (into [] (repeat 10 (into [] (repeat 9 "")))))

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

(deftest valid-elephant-moves-test
  (testing "all elephant moves"
    (let [board (assoc-in empty-board [7 4] "rE")]
      (is (= #{[5 2] [5 6] [9 2] [9 6]} (valid-elephant-moves board [7 4])))))
  (testing "cannot move when blocked"
    (let [board [["" "" "" "" ""]
                 ["" "bP" "" "" ""]
                 ["" "" "bE" "" ""]
                 ["" "" "" "bP" ""]
                 ["" "" "" "" ""]]]
      (is (= #{[0 4] [4 0]} (valid-elephant-moves board [2 2])))))
  (testing "cannot move off board"
    (let [board [["" "" "bE" "" ""]
                 ["" "" "" "" ""]
                 ["" "" "" "" ""]]]
      (is (= #{[2 0] [2 4]} (valid-elephant-moves board [0 2])))))
  (testing "red elephant cannot cross river"
    (let [board (assoc-in empty-board [5 6] "rE")]
      (is (= #{[7 4] [7 8]} (valid-elephant-moves board [5 6])))))
  (testing "black elephant cannot cross river"
    (let [board (assoc-in empty-board [4 6] "bE")]
      (is (= #{[2 4] [2 8]} (valid-elephant-moves board [4 6])))))
  (testing "can only capture enemy pieces"
    (let [board [["rP" "" "" "" "bE"]
                 ["" "" "" "" ""]
                 ["" "" "bE" "" ""]]]
      (is (= #{[0 0]} (valid-elephant-moves board [2 2]))))))

(deftest valid-moves-test
  (testing "no piece at location"
    (is (= #{} (valid-moves starting-position [1 0]))))
  (testing "horse moves"
    (with-redefs [valid-horse-moves (fn [board location] [board location])]
      (is (= [starting-position [9 7]] (valid-moves starting-position [9 7])))))
  (testing "elephant moves"
    (with-redefs [valid-elephant-moves (fn [board location] [board location])]
                 (is (= [starting-position [9 6]] (valid-moves starting-position [9 6]))))))
