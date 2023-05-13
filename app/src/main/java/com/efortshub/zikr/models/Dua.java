package com.efortshub.zikr.models;

import java.util.List;

public class Dua {
   /* {
  "details": [
    {
      "dua_global_id": "8",
      "dua_segment_id": "",
      "top": "",
      "arabic_diacless": "تبلي ويخلف اللٰه تعالٰى",
      "arabic": "تُبْلِي وَيُخْلِفُ اللّٰهُ تَعَالٰى",
      "transliteration": "তুবলী ওয়া ইয়ুখলিফুল্লা-হু তা‘আলা",
      "translations": "তুমি পুরাতন করে ফেলবে, আর মহান আল্লাহ এর স্থলাভিষিক্ত করবেন",
      "bottom": "",
      "reference": "সুনান আবি দাউদ ৪/৪১, হাদীস নং ৪০২০; দেখুন, সহীহ আবি দাউদ ২/৭৬০।"
    }
  ],
  "pageTitle": "অপরকে নতুন কাপড় পরিধান করতে দেখলে তার জন্য দো‘আ #১"
}*/

    List<DuaDetails> details;
    String pageTitle;

    public List<DuaDetails> getDetails() {
        return details;
    }

    public void setDetails(List<DuaDetails> details) {
        this.details = details;
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public void setPageTitle(String pageTitle) {
        this.pageTitle = pageTitle;
    }
}
