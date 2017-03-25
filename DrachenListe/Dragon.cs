﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Runtime.Serialization;
using Gtk;

namespace DragonImport {

  [DataContract]
  public class Dragon {

//    [DataMember]
//    public Dictionary<String, String> Names = new Dictionary<string,string>();

		public enum DragonTypeEnum {
			Common,
			Uncommon,
			Rare,
			Epic,
			Legendary,
			Boss
		}

    [DataMember]
    public string id;
    [DataMember]
    public DragonTypeEnum DragonType;
    [DataMember]
    public string Hatching;
    [DataMember]
    public string Breeding;
    [DataMember]
    public List<string> Elements;
    [DataMember]
    public bool isNormalBreadable;
    [DataMember]
    public bool isEnchatmentBreed;
    [DataMember]
    public bool isVip;
    [DataMember]
    public bool isUnreleased = false;





    [DataMember]
    public bool isLimitedTime;
    [DataMember]
    public bool isClanShop;
    [DataMember]
    public bool isDungeon;
    [DataMember]
    public bool isSeasonal;
    [DataMember]
    public bool isDailyQuestPuzzle;
    [DataMember]
    public bool isFriendShipTotem;
    [DataMember]
    public bool isReferal;
    [DataMember]
    public bool isDailyLogin;
    [DataMember]
    public int dailyLoginCount;
    [DataMember]
    public bool isEnchatmentLeague;
    [DataMember]
    public bool isArena;


    [DataMember]
    public string Gold;
    [DataMember]
    public string Attack;
    [DataMember]
    public string Health;
    [DataMember]
    public string Cost;
    [DataMember]
    public string CostType;
    [DataMember]
    public string icon;




    private int timestring2seconds(string s) {
      int retval = 0;
      try {
        string[] fracs = s.ToLower().Split(new char[] { ' ' });
        foreach (var f in fracs) {
          if (f.Contains("s")) {
            retval = retval + Convert.ToInt32(f.Replace("s", ""));
          } else if (f.Contains("m")) {
            retval = retval + Convert.ToInt32(f.Replace("m", "")) * 60;
          } else if (f.Contains("h")) {
            retval = retval + Convert.ToInt32(f.Replace("h", "")) * 60 * 60;
          } else if (f.Contains("d")) {
            retval = retval + Convert.ToInt32(f.Replace("d", "")) * 60 * 60 * 24;
          }
        }
      } catch (Exception ex) {
        System.Diagnostics.Debug.WriteLine(ex.Message, ex);
      }
      return retval;
    }

    public  string ToIonicClassString (){
      //     this.dragonList["fire"] = new Dragon("fire dragon",DragonRarity.COMMON,Element.FIRE,null,null,30,30);
      StringBuilder retval = new StringBuilder ();
      retval.AppendFormat("this.dragonList[\"{0}\"] =", id);
      retval.AppendFormat(" new Dragon(\"{0}\",", id);
     
      switch (DragonType) {
        case DragonTypeEnum.Common:
          retval.Append("DragonRarity.COMMON,");
          break;
        case DragonTypeEnum.Uncommon:
          retval.Append("DragonRarity.UNCOMMON,");
          break;
        case DragonTypeEnum.Rare:
          retval.Append("DragonRarity.RARE,");
          break;
        case DragonTypeEnum.Epic:
          retval.Append("DragonRarity.EPIC,");
          break;
        case DragonTypeEnum.Legendary:
          retval.Append("DragonRarity.LEGENDARY,");
          break;
        default:
          retval.Append("null,");
          break;
      } 
      if (Elements.Count > 0) {
        retval.AppendFormat("Element.{0},", Elements[0].ToUpperInvariant());
      } else {
        retval.Append("null,");
      }
      if (Elements.Count > 1) {
        retval.AppendFormat("Element.{0},", Elements[1].ToUpperInvariant());
      } else {
        retval.Append("null,");
      }
      if (Elements.Count > 2) {
        retval.AppendFormat("Element.{0},", Elements[2].ToUpperInvariant());
      } else {
        retval.Append("null,");
      }
      retval.AppendFormat("\"{0}\",\"{1}\");", Hatching, Breeding);
      return retval.ToString();
    }

    public override string ToString (){
			StringBuilder retval = new StringBuilder ();
			string csv = ";";
      retval.Append (id).Replace (" ", "_").Replace ("-", "_").Replace (".", "").Replace ("'", "").Append (csv);
			retval.Append (icon).Append (csv);
			retval.Append (DragonType.ToString ()).Append (csv);
			foreach (var e in Elements) {
				retval.Append (e).Append ("/");
			}
			retval.Remove (retval.Length - 1, 1);
			retval.Append (csv);
			retval.Append (Attack).Append (csv);
			retval.Append (Health).Append (csv);
			retval.Append (Gold).Append (csv);
			retval.Append (timestring2seconds (Breeding)).Append (csv);
			retval.Append (timestring2seconds (Hatching)).Append (csv);
			retval.Append (Cost).Append (csv);
			retval.Append (CostType).Append (csv);


			//   public bool isLimitedTime;
			//public bool isClanShop;
			//public bool isVip;
			//public bool isDungeon;
			//public bool isSeasonal;
			//public bool isDailyQuestPuzzle;
			//public bool isFriendShipTotem;
			//public bool isReferal;
			//public bool isDailyLogin;
			//public int dailyLoginCount;
			//public bool isEnchatmentBreed;
			//public bool isEnchatmentLeague;
			//public bool isArena;

			retval.Append (isLimitedTime).Append (csv);
			retval.Append (isClanShop).Append (csv);
			retval.Append (isVip).Append (csv);
			retval.Append (isDungeon).Append (csv);
			retval.Append (isSeasonal).Append (csv);
			retval.Append (isDailyQuestPuzzle).Append (csv);
			retval.Append (isFriendShipTotem).Append (csv);
			retval.Append (isReferal).Append (csv);
			retval.Append (isDailyLogin).Append (csv);
			retval.Append (dailyLoginCount).Append (csv);
			retval.Append (isEnchatmentBreed).Append (csv);
			retval.Append (isEnchatmentLeague).Append (csv);
      retval.Append (isArena).Append (csv);
      retval.Append (isUnreleased).Append (csv);
      retval.Append (isNormalBreadable).Append (csv);



			return retval.ToString ();
		}

	
	
  }
}
