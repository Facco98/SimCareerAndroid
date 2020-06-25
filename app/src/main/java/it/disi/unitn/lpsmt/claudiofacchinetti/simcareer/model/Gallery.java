package it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.model;

import it.disi.unitn.lpsmt.claudiofacchinetti.simcareer.util.Constants;

import java.util.List;

public class Gallery {

    private String name;
    private List<String> imgsPaths;

    public Gallery(String name, List<String> imgPaths){

        this.name = name;
        this.imgsPaths = imgPaths;

    }

    public String getImgPath(int i){

        return this.imgsPaths.get(i);

    }

    public String getName() {
        return name;
    }

    public String getImgThumbPath(int i){

        return Constants.ASSETS_GALLERY_THUMB + "/" +this.imgsPaths.get(i);

    }

    public String getImgFullPath( int i ){

        return Constants.ASSETS_GALLERY_NAME + "/" + this.name + "/" + this.getImgPath(i);

    }

    public String getThumbFullPath( int i ){

        return Constants.ASSETS_GALLERY_NAME + "/" + this.name + "/" + Constants.ASSETS_GALLERY_THUMB + "/" +this.getImgPath(i);

    }

    @Override
    public String toString(){

        return "[ name: "+this.name+"\n paths: " + this.imgsPaths.toString() + " ]";

    }

    public int count(){
        return this.imgsPaths.size();
    }



}
