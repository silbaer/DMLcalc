using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Text.RegularExpressions;

namespace DragonImport {
  public class MobaImport {

    public static List<Dragon> import(string data) {




			List<Dragon> retval = new List<Dragon> ();
		
			string[] splits = data.Split (new String[] { Environment.NewLine }, StringSplitOptions.None);
			foreach (var line in splits) {
				string[] lineSplits = line.Split (new char[] { '\t' });
				if (lineSplits.Count () >= 11) {
					try {
						Dragon d = new Dragon ();
						//dragon / ID
						d.id = lineSplits [0];
						// icon
						d.icon = lineSplits [1];
						// Type
						if (lineSplits [2].StartsWith ("Common", StringComparison.InvariantCultureIgnoreCase)) {
							d.DragonType = Dragon.DragonTypeEnum.Common;
						}
						if (lineSplits [2].StartsWith ("Uncommon", StringComparison.InvariantCultureIgnoreCase)) {
							d.DragonType = Dragon.DragonTypeEnum.Uncommon;
						}
						if (lineSplits [2].StartsWith ("Rare", StringComparison.InvariantCultureIgnoreCase)) {
							d.DragonType = Dragon.DragonTypeEnum.Rare;
						}
						if (lineSplits [2].StartsWith ("Epic", StringComparison.InvariantCultureIgnoreCase)) {
							d.DragonType = Dragon.DragonTypeEnum.Epic;
						}
						if (lineSplits [2].StartsWith ("Legendary", StringComparison.InvariantCultureIgnoreCase)) {
							d.DragonType = Dragon.DragonTypeEnum.Legendary;
						}
            if (lineSplits [2].StartsWith ("Boss", StringComparison.InvariantCultureIgnoreCase)) {
              d.DragonType = Dragon.DragonTypeEnum.Boss;
            }
            if (lineSplits [2].StartsWith ("Divine", StringComparison.InvariantCultureIgnoreCase)) {
              d.DragonType = Dragon.DragonTypeEnum.Divine;
            }
            if(string.IsNullOrWhiteSpace(lineSplits[3]) ){
              d.isNormalBreadable = true;
            } else {
              d.isNormalBreadable = false;
            }
						// Notes
						if (lineSplits [3].Contains ("LTD Icon")) {
							d.isLimitedTime = true;
						}
						if (lineSplits [3].Contains ("Clan Icon")) {
							d.isClanShop = true;
						}
						if (lineSplits [3].Contains ("Referral Icon")) {
							d.isReferal = true;
						}
						if (lineSplits [3].Contains ("Puzzle Icon")) {
							d.isDailyQuestPuzzle = true;
						}
						if (lineSplits [3].Contains ("7 Icon")) {
							d.isDailyLogin = true;
							d.dailyLoginCount = 7;
						}
						if (lineSplits [3].Contains ("30 Icon")) {
							d.isDailyLogin = true;
							d.dailyLoginCount = 30;
						}
						if (lineSplits [3].Contains ("60 Icon")) {
							d.isDailyLogin = true;
							d.dailyLoginCount = 60;
						}
						if (lineSplits [3].Contains ("120 Icon")) {
							d.isDailyLogin = true;
							d.dailyLoginCount = 120;
						}
						if (lineSplits [3].Contains ("260 Icon")) {
							d.isDailyLogin = true;
							d.dailyLoginCount = 260;
						}
						if (lineSplits [3].Contains ("Dungeon Chest")) {
							d.isSeasonal = true;
						}
						if (lineSplits [3].Contains ("Clockwork Dungeon Submarine")) {
							d.isDungeon = true;
						}
						if (lineSplits [3].Contains ("Diamond Icon")) {
							d.isArena = true;
						}
						if (lineSplits [3].Contains ("Friendship Point")) {
							d.isFriendShipTotem = true;
						}
						if (lineSplits [3].Contains ("Silver Icon")) {
							d.isArena = true;
						}
						if (lineSplits [3].Contains ("Bronze Icon")) {
							d.isArena = true;
						}
						if (lineSplits [3].Contains ("Golden Icon")) {
							d.isArena = true;
						}
						if (lineSplits [3].Contains ("Dragonscale Icon")) {
							d.isArena = true;
						}
						if (lineSplits [3].Contains ("Limited Arena Prize Icon")) {
							d.isArena = true;
						}
						if (lineSplits [3].Contains ("VIP")) {
							d.isVip = true;
						}
            if (lineSplits [3].Contains ("Enchanted Breeding")) {
							d.isEnchatmentBreed = true;
						}
            if (lineSplits [3].Contains ("League Button")) {
							d.isEnchatmentLeague = true;
						}
						// elements
						string elements = lineSplits [4].Replace ("(Element) Icon.png", " ");
            Regex  r = new Regex("([A-Z])");
            elements = r.Replace(elements," $1");
            

						d.Elements = new List<string> (elements.Split (new char[]{ ' ' }, StringSplitOptions.RemoveEmptyEntries));
						// breeding
						d.Breeding = lineSplits [5];
						// Hatching
						d.Hatching = lineSplits [6];
						// cost
						string cost = lineSplits [7].Replace ("Icon.png", "").Replace(",","");
						string[] costSplits = cost.Split (new char[]{ ' ' }, StringSplitOptions.RemoveEmptyEntries);
						if(costSplits.Count() == 2){
						d.CostType = costSplits [1];
						d.Cost = (costSplits [0]);
						}
						// health
						d.Health = (lineSplits [8]);
						// attack
						d.Attack = (lineSplits [9]);
						// gold
					
						d.Gold =  (lineSplits [10]);
						
						retval.Add (d);
					} catch (Exception e) {
						System.Diagnostics.Debug.WriteLine (e.Message, e);
					}

				}
			}
			return retval;
		}

  }
}
