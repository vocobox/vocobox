package org.vocobox.model.note;

public enum Voyel {
    A, E, I, O, OU, U;
    
    public static Voyel parse(String voyel){
        for(Voyel v: Voyel.values()){
            if(v.toString().equals(voyel.toUpperCase())){
                return v;
            }
        }
        System.out.println("did not parse " + voyel);
        return null;
        //throw new IllegalArgumentException("not a voyel : " + voyel + ". Try among " + Voyel.values());
    }
}
