using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Runtime.Serialization;
using System.Text;
using System.Xml;
using Gtk;

namespace DragonImport {
  [DataContract]
  public class DragonList  {

    [DataMember]
    private Dictionary<string, Dragon> _dragonList;

    public DragonList() {
      _dragonList = new Dictionary<string, Dragon>();
    }

    public void Add(Dragon d) {
      d.id = d.id.ToLower();
      _dragonList.Add(d.id, d);
    }

    [IgnoreDataMember]
    public List<Dragon> Values {
      get { return _dragonList.Values.ToList(); }
    }

    public void SaveToFile(string Filename) {
      XmlWriterSettings settings = new XmlWriterSettings();
      settings.Indent = true;
      

      FileStream fs = new FileStream(Filename, FileMode.Create);
      XmlWriter xwriter = XmlWriter.Create(fs, settings);

   //   XmlDictionaryWriter writer = XmlDictionaryWriter.CreateDictionaryWriter(xwriter);

      DataContractSerializer ser = new DataContractSerializer(this.GetType());
      ser.WriteObject(xwriter, this);
      //Console.WriteLine("Finished writing object.");
      xwriter.Close();
      fs.Close();
    }

    public static DragonList LoadFromFile(string Filename) {
      DragonList retval;

      XmlReaderSettings settings = new XmlReaderSettings();
     

      FileStream fs = new FileStream(Filename, FileMode.Open);
      XmlReader xReader = XmlReader.Create(fs);
      DataContractSerializer ser = new DataContractSerializer(typeof(DragonList));
      retval = ser.ReadObject(xReader) as DragonList;
      xReader.Close();
      fs.Close();

      return retval;
    }

    public bool Contains(string dragonId) {
      return _dragonList.ContainsKey(dragonId.ToLower());
    }
    public bool Contains(Dragon dragon) {
      return _dragonList.ContainsKey(dragon.id.ToLower());
    }

	  public ListStore getModel(){
      var retval = new ListStore(typeof(Dragon));
      foreach (var kv in _dragonList) {
        retval.AppendValues(kv.Value);
      }
      return retval;
    }
	}
}
