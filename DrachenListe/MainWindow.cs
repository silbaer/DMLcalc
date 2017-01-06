using System;
using Gtk;
using System.Collections.Generic;
using DragonImport;
using System.Text;

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

    Gtk.TreeViewColumn idColumn = new Gtk.TreeViewColumn ();
    idColumn.Title = "id";
    Gtk.CellRendererText idCell = new Gtk.CellRendererText ();
    idColumn.PackStart (idCell, true);

    Gtk.TreeViewColumn iconColumn = new Gtk.TreeViewColumn ();
    iconColumn.Title = "icon";
    Gtk.CellRendererText iconCell = new Gtk.CellRendererText ();
    iconColumn.PackStart (iconCell, true);


   
//    idColumn.SetCellDataFunc(idCell, new TreeCellDataFunc(renderDragonID));
//    this.treeview1.AppendColumn(idColumn);

//    iconColumn.SetCellDataFunc(iconCell, new TreeCellDataFunc(renderDragonIconText));
//    this.treeview1.AppendColumn(iconColumn);
    this.treeview1.AppendColumn(getCol("id"));
    this.treeview1.AppendColumn(getCol("Breeding"));
    this.treeview1.AppendColumn(getCol("Hatching"));
	}

  private void renderDragonID(TreeViewColumn col, CellRenderer c, TreeModel m, TreeIter i){
   
    Dragon d = (Dragon)m.GetValue(i, 0);
    (c as CellRendererText).Text = d.id;
  }
  private void renderDragonIconText(TreeViewColumn col, CellRenderer c, TreeModel m, TreeIter i){
    Dragon d = (Dragon)m.GetValue(i, 0);
    (c as CellRendererText).Text = d.icon;
  }

  private Gtk.TreeViewColumn getCol(string title){
    Gtk.TreeViewColumn retval = new TreeViewColumn();
    retval.Title = title;
    Gtk.CellRendererText cr = new Gtk.CellRendererText ();
    retval.PackStart (cr, true);
    retval.SetCellDataFunc(cr, new TreeCellDataFunc(renderDragonCell));
    return retval;
  }


  private void renderDragonCell(TreeViewColumn col, CellRenderer c, TreeModel m, TreeIter i){
    Dragon d = (Dragon)m.GetValue(i, 0);
    string val="default";
    if (col.Title == "id") {
      val = d.id;
    } else if (col.Title == "Hatching") {
      val = d.Hatching;
    } else if (col.Title == "Breeding") {
      val = d.Breeding;
    }

    (c as CellRendererText).Text = val;
  }



}
