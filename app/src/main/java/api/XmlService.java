package api;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by Administrator on 2017/10/18.
 */

@Root(name = "maixiang", strict = false)
public class XmlService {
    @Element(name = "result")
    public String result;
    @Element(name = "infor")
    public String infor;
    @Element(name = "xinmai")
    public int xinmai;
    @Element(name = "shenmai")
    public int shenmai;
    @Element(name = "ganmai")
    public int ganmai;
    @Element(name = "deviceid")
    public String deviceid;


}
