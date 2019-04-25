(ns bughouse.moves-test
  (:require
    [cljs.test :refer-macros [deftest is testing]]
    [bughouse.core :refer [starting-position]]
    [bughouse.moves :refer [valid-horse-moves valid-moves]]))

(deftest valid-horse-moves-test
  (testing "horse moves from starting position"
    (is (= #{[7 6] [7 8]} (valid-horse-moves starting-position [9 7]))))
  (testing "horse moves after H8+7"
    (let [position (-> starting-position
                       (assoc-in [7 6] "rH")
                       (assoc-in [9 7] ""))]
      (is (= #{[8 4] [9 7]} (valid-horse-moves position [7 6])))))
  (testing "taking enemy pieces"
    (let [position (assoc-in starting-position [1 8] "rH")]
      (is (= #{[0 6] [2 6] [3 7]} (valid-horse-moves position [1 8]))))))

(deftest valid-moves-test
  (testing "no piece at location"
    (is (= #{} (valid-moves starting-position [1 0]))))
  (testing "horse moves"
    (is (= (valid-horse-moves starting-position [9 7]) (valid-moves starting-position [9 7])))))
