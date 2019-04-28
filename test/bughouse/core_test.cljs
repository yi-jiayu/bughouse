(ns bughouse.core-test
  (:require
    [cljs.test :refer-macros [deftest is testing]]
    [bughouse.core :refer [square]]))

(deftest square-test
  (testing "no piece"
    (is (= [:div {:class         "square"
                  :data-piece    nil
                  :data-side     nil
                  :data-selected false
                  :on-click      nil}]
           (square "" false false nil)))))
