(ns ^:figwheel-no-load devcon.dev
  (:require
    [devcon.core :as core]
    [devtools.core :as devtools]))


(enable-console-print!)

(devtools/install!)

(core/init!)
