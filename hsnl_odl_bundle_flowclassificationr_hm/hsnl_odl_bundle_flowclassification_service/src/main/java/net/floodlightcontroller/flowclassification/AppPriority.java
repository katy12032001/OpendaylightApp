/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package net.floodlightcontroller.flowclassification;

import java.io.Serializable;

public class AppPriority implements Serializable{
    private String AppName;
    private String Priority;
    
     public AppPriority(String appName, String priority){
        this.AppName = appName;
        this.Priority = priority;
     }
     
     public void setAppName(String appname){
         this.AppName = appname;
     }
     
     public void setPriority(String pri){
         this.Priority = pri;
     }
     
     public String getAppName(){
         return this.AppName;
     }
     
     public String getPriority(){
         return this.Priority;
     }
}
