package com.inlab.racodemoapi.Models;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by florencia.rimolo on 14/02/2017.
 */

public class PlansEstudiModel {
        @SerializedName("count")
        @Expose
        private Integer count;
        @SerializedName("next")
        @Expose
        private Object next;
        @SerializedName("previous")
        @Expose
        private Object previous;
        @SerializedName("results")
        @Expose
        private List<PlansEstudiResult> results = null;

        public Integer getCount() {
            return count;
        }

        public void setCount(Integer count) {
            this.count = count;
        }

        public Object getNext() {
            return next;
        }

        public void setNext(Object next) {
            this.next = next;
        }

        public Object getPrevious() {
            return previous;
        }

        public void setPrevious(Object previous) {
            this.previous = previous;
        }

        public List<PlansEstudiResult> getResults() {
            return results;
        }

        public void setResults(List<PlansEstudiResult> results) {
            this.results = results;
        }
}


