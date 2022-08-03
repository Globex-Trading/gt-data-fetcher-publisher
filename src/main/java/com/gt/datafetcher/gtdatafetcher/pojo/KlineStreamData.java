package com.gt.datafetcher.gtdatafetcher.pojo;

public class KlineStreamData {
    public String stream;
    public Payload data;

    public class Payload {
        public String e;
        public long E;
        public String s;
        public PriceData k;

        public class PriceData {
            public long t;
            public long T;
            public String s;
            public String i;
            public String f;
            public String L;
            public float o;
            public float c;
            public float h;
            public float l;
            public float v;
            public String n;
            public String x;
            public String q;
            public String V;
            public String Q;
            public String B;
        }
    }
}
