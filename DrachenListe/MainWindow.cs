using System;
using Gtk;
using System.Collections.Generic;
using DragonImport;
using System.Text;
using System.Reflection;

public partial class MainWindow: Gtk.Window
{
	DragonList dragons = new DragonList();
	public MainWindow () : base (Gtk.WindowType.Toplevel)
	{
		Build ();


	}

	protected void OnDeleteEvent (object sender, DeleteEventArgs a)
	{
		Application.Quit ();
		a.RetVal = true;
  }

  protected void Import_Click2(object sender, EventArgs e)
  {
    List<Dragon> lst = MobaImport.import(this.txtImport.Buffer.Text);

    foreach (var d in lst) {
      string id = d.id.ToLower();
      if (!id.Equals("dragon") && !dragons.Contains(id)) {
        dragons.Add(d);
      }
    }
    StringBuilder sb = new StringBuilder();
    foreach (var d in dragons.Values) {
      sb.Append(d.ToIonicClassString()).Append(Environment.NewLine);
    }
    txtDragons.Buffer.Text = sb.ToString();

  }

  protected void Import_Click(object sender, EventArgs e)
  {
		List<Dragon> lst = MobaImport.import(this.txtImport.Buffer.Text);

		foreach (var d in lst) {
			string id = d.id.ToLower();
			if (!id.Equals("dragon") && !dragons.Contains(id)) {
				dragons.Add(d);
			}
		}

//		dragons.SaveToFile("dragons.xml");
//
		StringBuilder sb = new StringBuilder();
		foreach (var d in dragons.Values) {
			sb.Append(d.ToString()).Append(Environment.NewLine);
		}
		txtDragons.Buffer.Text = sb.ToString();

    this.treeview1.Model = dragons.getModel();
//
//    Gtk.TreeViewColumn idColumn = new Gtk.TreeViewColumn ();
//    idColumn.Title = "id";
//    Gtk.CellRendererText idCell = new Gtk.CellRendererText ();
//    idColumn.PackStart (idCell, true);
//
//    Gtk.TreeViewColumn iconColumn = new Gtk.TreeViewColumn ();
//    iconColumn.Title = "icon";
//    Gtk.CellRendererText iconCell = new Gtk.CellRendererText ();
//    iconColumn.PackStart (iconCell, true);


   
//    idColumn.SetCellDataFunc(idCell, new TreeCellDataFunc(renderDragonID));
//    this.treeview1.AppendColumn(idColumn);

//    iconColumn.SetCellDataFunc(iconCell, new TreeCellDataFunc(renderDragonIconText));
//    this.treeview1.AppendColumn(iconColumn);

//    this.treeview1.AppendColumn(getCol("id"));
//    this.treeview1.AppendColumn(getCol("Breeding"));
//    this.treeview1.AppendColumn(getCol("Hatching"));
//    this.treeview1.AppendColumn(getCol("isLimitedTime"));
//    this.treeview1.AppendColumn(getCol("isClanShop"));
//    this.treeview1.AppendColumn(getCol("isVip"));
//    this.treeview1.AppendColumn(getCol("isDungeon"));
//    this.treeview1.AppendColumn(getCol("isSeasonal"));
//    this.treeview1.AppendColumn(getCol("isDailyQuestPuzzle"));
//    this.treeview1.AppendColumn(getCol("isFriendShipTotem"));
//    this.treeview1.AppendColumn(getCol("isReferal"));
//    this.treeview1.AppendColumn(getCol("isDailyLogin"));
//    this.treeview1.AppendColumn(getCol("dailyLoginCount"));
//    this.treeview1.AppendColumn(getCol("isEnchatmentBreed"));
//    this.treeview1.AppendColumn(getCol("isEnchatmentLeague"));
//    this.treeview1.AppendColumn(getCol("isArena"));
//    this.treeview1.AppendColumn(getCol("DragonType"));
//    this.treeview1.AppendColumn(getCol("Attack"));
//    this.treeview1.AppendColumn(getCol("Health"));
//    this.treeview1.AppendColumn(getCol("Cost"));
//    this.treeview1.AppendColumn(getCol("CostType"));
//    this.treeview1.AppendColumn(getCol("Elements"));
//    this.treeview1.AppendColumn(getCol("isUnreleased"));
//    this.treeview1.AppendColumn(getCol("isNormalBreadable"));

    Dragon data = new Dragon();
     
    foreach (FieldInfo info in typeof(Dragon).GetFields())
    {
      this.treeview1.AppendColumn(getCol(info));
    //  System.Diagnostics.Debug.WriteLine(info.Name);  
    }   



  }

 

//  private void renderDragonID(TreeViewColumn col, CellRenderer c, TreeModel m, TreeIter i){
//   
//    Dragon d = (Dragon)m.GetValue(i, 0);
//    (c as CellRendererText).Text = d.id;
//  }
//  private void renderDragonIconText(TreeViewColumn col, CellRenderer c, TreeModel m, TreeIter i){
//    Dragon d = (Dragon)m.GetValue(i, 0);
//    (c as CellRendererText).Text = d.icon;
//  }
  private Gtk.TreeViewColumn getCol(FieldInfo info){
    Gtk.TreeViewColumn retval = new TreeViewColumn();
    retval.Title = info.Name;

    Gtk.CellRenderer cellRenderer;

    if (info.FieldType == typeof(bool)) {
      cellRenderer = new CellRendererToggle();
      (cellRenderer as CellRendererToggle).Toggled += toggelcellEdited ;
    } else {
      cellRenderer = new CellRendererText();
      (cellRenderer as CellRendererText).Edited += textcellEdited;
    }
//    cellRenderer.Edited += textcellEdited;
//    cellRenderer.EditingStarted += Cr_EditingStarted;
//    cellRenderer.EditingCanceled += Cr_EditingCanceled;
    cellRenderer.Data.Add("_colTitle", retval.Title);
    retval.PackStart (cellRenderer, true);
    retval.SetCellDataFunc(cellRenderer, new TreeCellDataFunc(renderDragonCell));
    return retval;
  }


  private Gtk.TreeViewColumn getCol(string title){
    Gtk.TreeViewColumn retval = new TreeViewColumn();
    retval.Title = title;
    Gtk.CellRendererText cr = new Gtk.CellRendererText ();
    cr.Edited += textcellEdited;
//    cr.EditingStarted += Cr_EditingStarted;
//    cr.EditingCanceled += Cr_EditingCanceled;
    cr.Data.Add("_colTitle", title);
    retval.PackStart (cr, true);
    retval.SetCellDataFunc(cr, new TreeCellDataFunc(renderDragonCell));
    return retval;
  }

 
  void toggelcellEdited (object o, ToggledArgs args)
  {

    TreePath path = new TreePath (args.Path);
    //    int i = path.Indices[0];
    TreeModel m = this.treeview1.Model;
    TreeIter iter;
    m.GetIter (out iter, path);

    Dragon d = (Dragon) m.GetValue(iter, 0);

    string colTitle = string.Empty;
    if ((o as CellRenderer).Data.ContainsKey("_colTitle")) {
      colTitle = Convert.ToString((o as CellRenderer).Data["_colTitle"]);
    }
    System.Diagnostics.Debug.WriteLine(colTitle);


  }

  void textcellEdited (object o, EditedArgs args)
  {
 

    TreePath path = new TreePath (args.Path);
//    int i = path.Indices[0];
    TreeModel m = this.treeview1.Model;
    TreeIter iter;
    m.GetIter (out iter, path);

    Dragon d = (Dragon) m.GetValue(iter, 0);
   

    string colTitle = string.Empty;
    if ((o as CellRenderer).Data.ContainsKey("_colTitle")) {
      colTitle = Convert.ToString((o as CellRenderer).Data["_colTitle"]);
    }
    System.Diagnostics.Debug.WriteLine(colTitle);

    
  }


  private void renderDragonCell(TreeViewColumn col, CellRenderer c, TreeModel m, TreeIter i){
    Dragon d = (Dragon)m.GetValue(i, 0);
    object val="default";
//    if (col.Title == "id") {
//      val = d.id;
//    } else if (col.Title == "Hatching") {
//      val = d.Hatching;
//    } else if (col.Title == "Breeding") {
//      val = d.Breeding;
//    } else if (col.Title == "isLimitedTime") {
//      val = d.isLimitedTime;
//    } else if (col.Title == "isClanShop") {
//      val = d.isClanShop;
//    } else if (col.Title == "isVip") {
//      val = d.isVip;
//    } else if (col.Title == "isDungeon") {
//      val = d.isDungeon;
//    } else if (col.Title == "isSeasonal") {
//      val = d.isSeasonal;
//    } else if (col.Title == "isDailyQuestPuzzle") {
//      val = d.isDailyQuestPuzzle;
//    } else if (col.Title == "isFriendShipTotem") {
//      val = d.isFriendShipTotem;
//    } else if (col.Title == "isReferal") {
//      val = d.isReferal;
//    } else if (col.Title == "isDailyLogin") {
//      val = d.isDailyLogin;
//    } else if (col.Title == "dailyLoginCount") {
//      val = d.dailyLoginCount;
//    } else if (col.Title == "isEnchatmentBreed") {
//      val = d.isEnchatmentBreed;
//    } else if (col.Title == "isEnchatmentLeague") {
//      val = d.isEnchatmentLeague;
//    } else if (col.Title == "isArena") {
//      val = d.isArena;
//    } else if (col.Title == "DragonType") {
//      val = d.DragonType;
//    } else if (col.Title == "Gold") {
//      val = d.Gold;
//    } else if (col.Title == "Attack") {
//      val = d.Attack;
//    } else if (col.Title == "Health") {
//      val = d.Health;
//    } else if (col.Title == "Cost") {
//      val = d.Cost;
//    } else if (col.Title == "CostType") {
//      val = d.CostType;
//    } else if (col.Title == "Elements") {
//      val = d.Elements;
//    } else if (col.Title == "icon") {
//      val = d.icon;
//    } else if (col.Title == "isUnreleased") {
//      val = d.isUnreleased;
//    } else if (col.Title == "isNormalBreadable") {
//      val = d.isNormalBreadable;
//    }

    FieldInfo fi = d.GetType().GetField(col.Title);
    if (fi != null) {
      val = fi.GetValue(d);
      if (c is CellRendererText) {
        var textCell = (CellRendererText)c;
        textCell.Text = Convert.ToString(val);
        textCell.Editable = true;
      } else if (c is CellRendererToggle) {
        var ct = (CellRendererToggle)c;
        ct.Active = Convert.ToBoolean(val);
        ct.Activatable = true;
      }
    }


   // (c as CellRendererText).Text =  Convert.ToString( val);
  }



}
