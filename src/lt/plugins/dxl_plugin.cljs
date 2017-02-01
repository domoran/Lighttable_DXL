(ns lt.plugins.dxl-plugin
  (:require [lt.object :as object]
            [lt.objs.tabs :as tabs]
            [lt.objs.command :as cmd])
  (:require-macros [lt.macros :refer [defui behavior]]))

(defui hello-panel [this]
  [:h1 "Hello from dxl-plugin"])

(object/object* ::dxl-plugin.hello
                :tags [:dxl-plugin.hello]
                :behaviors [::on-close-destroy]
                :name "dxl-plugin"
                :init (fn [this]
                        (hello-panel this)))

(behavior ::on-close-destroy
          :triggers #{:close}
          :desc "dxl-plugin: Close tab and tabset as well if last tab"
          :reaction (fn [this]
                      (when-let [ts (:lt.objs.tabs/tabset @this)]
                        (when (= (count (:objs @ts)) 1)
                          (tabs/rem-tabset ts)))
                      (object/raise this :destroy)))

(def hello (object/create ::dxl-plugin.hello))

(cmd/command {:command ::say-hello
              :desc "dxl-plugin: Say Hello!!!"
              :exec (fn []
                      (tabs/add-or-focus! hello))})
