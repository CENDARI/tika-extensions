package fr.inria.aviz.tikaextensions.tika;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

//Add to the list all "values" based on which one writes to field @CendariProperties.PROVIDER which should return text & nerd as empty strings
public class ExcludeCendariIndexer {
  
  private static List<String> excludeList = 
        new ArrayList<String>(
            Arrays.asList(
                "http://data.theeuropeanlibrary.org/Agent/The_European_Library"
  ));
  
  
  public static boolean shouldExclude (String[] valuesToCheck) {
        if (valuesToCheck == null || valuesToCheck.length == 0) {
          return false;
        }

        return !Collections.disjoint(excludeList, Arrays.asList(valuesToCheck));
  }
  

}
