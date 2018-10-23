/**
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.dietncure.friendlychatapp;

public class FriendlyMessage {

    private String text;
    private String name;
    private String photoUrl;
    private String pdfUrl;
    private String time;
    private int TAG_VIEW;


    public FriendlyMessage() {
    }

    public FriendlyMessage(String text, String name, String photoUrl,String pdfUrl,String time,int TAG_VIEW) {
        this.text = text;
        this.name = name;
        this.photoUrl = photoUrl;
        this.pdfUrl = pdfUrl;
        this.time = time;
        this.TAG_VIEW =TAG_VIEW;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTAG_VIEW() {
        return TAG_VIEW;
    }

    public void setTAG_VIEW(int TAG_VIEW) {
        this.TAG_VIEW = TAG_VIEW;
    }

}
