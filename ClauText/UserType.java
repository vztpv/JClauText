package ClauText;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.regex.Pattern;

public class UserType extends Type {
	public void PushComment(String comment)
	{
		commentList.add(comment);
	}
	public int GetCommentListSize() { return commentList.size(); }
	public String GetCommentList(int idx)  { return commentList.get(idx); }
	public int GetIListSize() { return ilist.size(); }
	public int GetItemListSize() { return itemList.size(); }
	public int GetUserTypeListSize() { return userTypeList.size(); }
	public ItemType GetItemList(int idx) { return itemList.get(idx); }
	public UserType GetUserTypeList(int idx) { return userTypeList.get(idx); }

	public boolean IsItemList(int idx) 
	{
		return ilist.get(idx) == 1;
	}
	public boolean IsUserTypeList(int idx) 
	{
		return ilist.get(idx) == 2;
	}

	public UserType GetParent() { return parent; } // when used?
	
	private UserType parent;
	private ArrayList<String> commentList;
	private ArrayList<Integer> ilist;
	private ArrayList< ItemType > itemList;
	private ArrayList< UserType > userTypeList;

	public UserType() { super(""); parent = null; }
	public UserType(String name) 
	{
		super(name);
		parent = null;
	}
	
	public UserType(UserType ut) {
		super(ut.GetName());
		Reset(ut);  // Initial
	}

	private void Reset(UserType ut) {
		ilist = ut.ilist;
		itemList = ut.itemList;
		parent = ut.parent;
		commentList = ut.commentList;
		userTypeList = ut.userTypeList;
	}
	
	/// val : 1 or 2
	private int _GetIndex(ArrayList<Integer> ilist, int val)
	{
		return _GetIndex(ilist, val, 0);
	}
	private int _GetIndex(ArrayList<Integer> ilist,  int val, int start) {
		for (int i = start; i < ilist.size(); ++i) {
			if (ilist.get(i) == val) { return i; }
		}
		return -1;
	}
// test? - need more thinking!
	private int _GetItemIndexFromIlistIndex( ArrayList<Integer> ilist,  int ilist_idx) {
		if (ilist.size() == ilist_idx) { return ilist.size(); }
		int idx = _GetIndex(ilist, 1, 0);
		int item_idx = -1;

		while (idx != -1) {
			item_idx++;
			if (ilist_idx == idx) { return item_idx; }
			idx = _GetIndex(ilist, 1, idx + 1);
		}

		return -1;
	}
	private int _GetUserTypeIndexFromIlistIndex( ArrayList<Integer> ilist,  int ilist_idx) {
		if (ilist.size() == ilist_idx) { return ilist.size(); }
		int idx = _GetIndex(ilist, 2, 0);
		int usertype_idx = -1;

		while (idx != -1) {
			usertype_idx++;
			if (ilist_idx == idx) { return usertype_idx; }
			idx = _GetIndex(ilist, 2, idx + 1);
		}

		return -1;
	}
	/// type : 1 or 2
	private int _GetIlistIndex( ArrayList<Integer> ilist,  int index,  int type) {
		int count = -1;

		for (int i = 0; i < ilist.size(); ++i) {
			if (ilist.get(i) == type) {
				count++;
				if (index == count) {
					return i;
				}
			}
		}
		return -1;
	}

	public void RemoveItemList(int idx)
	{
		itemList.remove(idx);
		
		int count = 0;
		for (int i = 0; i < ilist.size(); ++i) {
			if (ilist.get(i) == 1) { count++; }
			if (count == idx + 1) {
				ilist.remove(i);
				break;
			}
		}
	}

	public void RemoveUserTypeList(int idx)
	{
		userTypeList.remove(idx);
		
		int count = 0;
		for (int i = 0; i < ilist.size(); ++i) {
			if (ilist.get(i) == 2) { count++; }
			if (count == idx + 1) {
				ilist.remove(i);
				break;
			}
		}
	}
	public void RemoveItemList(String varName)
	{
		int k = _GetIndex(ilist, 1, 0);
		
		for (int i = 0; i < itemList.size(); ++i) {
			if (false == varName.equals(itemList.get(i).GetName())) {
				k = _GetIndex(ilist, 1, k + 1);
			}
			else {
				ilist.remove(k);
				k = _GetIndex(ilist, 1, k);
			}
		}
	}
	public void RemoveItemList() /// ALL
	{
		itemList.clear();
		
		for (int i = 0; i < ilist.size();) {
			if (ilist.get(i) == 1)
			{
				ilist.remove(i);
			}
			else {
				++i;
			}
		}
	}
	public void RemoveEmptyItem() // fixed..
	{
		int k = _GetIndex(ilist, 1, 0);
		
		for (int i = 0; i < itemList.size(); ++i) {
			if (itemList.get(i).size() > 0) {
				k = _GetIndex(ilist, 1, k  + 1);
			}
			else {
				ilist.remove(k);
				k = _GetIndex(ilist, 1, k);
			}
		}
	}
	public void Remove()
	{
		/// parent.reUserType(name); - ToDo - X
		ilist.clear();
		itemList.clear();
		userTypeList.clear();
		commentList.clear();
	}
	public void RemoveUserTypeList() { /// chk memory leak test!!
		for (int i = 0; i < userTypeList.size(); i++) {
			if (null != userTypeList.get(i)) {
				userTypeList.set(i, null);
			}
		}
		// DO Empty..
		userTypeList.clear();

		for (int i = 0; i < ilist.size(); ) {
			if (ilist.get(i) == 1) {
				++i;
			}
			else {
				ilist.remove(i);
			}
		}
	}
	
	public void RemoveUserTypeList(String varName)
	{
		int k = _GetIndex(ilist, 2, 0);

		for (int i = 0; i < userTypeList.size(); ++i) {
			if (false == varName.equals(userTypeList.get(i).GetName())) {
				k = _GetIndex(ilist, 2, k + 1);
			}
			else {
				// re usertypeitem, ilist left shift 1.
				ilist.remove(k);
				k = _GetIndex(ilist, 2, k);
			}
		}
	}

	public void RemoveList(int idx) // ilist_idx!
	{
		// chk whether item or usertype.
		// find item_idx or usertype_idx.
		// re item or re usertype.
		if (ilist.get(idx) == 1) {
			int item_idx = -1;

			for (int i = 0; i < ilist.size() && i <= idx; ++i) {
				if (ilist.get(i) == 1) { item_idx++; }
			}

			RemoveItemList(item_idx);
		}
		else {
			int usertype_idx = -1;

			for (int i = 0; i < ilist.size() && i <= idx; ++i) {
				if (ilist.get(i) == 2) { usertype_idx++; }
			}

			RemoveUserTypeList(usertype_idx);
		}
	}

	public boolean empty() { return ilist.isEmpty(); }
	
	// chk
	public void InsertItemByIlist(int ilist_idx, String name, String item) {
		ilist.add(1);
		for (int i = ilist.size()-1; i > ilist_idx; --i) {
			ilist.set(i, ilist.get(i - 1));
		}
		ilist.set(ilist_idx, 1);


		int itemIndex = _GetItemIndexFromIlistIndex(ilist, ilist_idx);

		itemList.add(null);

		if (itemIndex != -1) {
			for (int i = itemList.size() - 1; i > itemIndex; --i) {
				itemList.set(i, itemList.get(i - 1));
			}
			itemList.set(itemIndex, new ItemType(name, item));
		}
		else {
			itemList.set(0, new ItemType(name, item)); // chk!!
		}	
	}
		// chk
	public void InsertUserTypeByIlist(int ilist_idx, UserType item) {
		ilist.add(2);
		UserType temp = item;

		temp.parent = this;

		for (int i = ilist.size() - 1; i > ilist_idx; --i) {
			ilist.set(i, ilist.get(i - 1));
		}
		ilist.set(ilist_idx, 2);

		int userTypeIndex = _GetUserTypeIndexFromIlistIndex(ilist, ilist_idx);
		userTypeList.add(null);
		if (userTypeIndex != -1) {
			for (int i = userTypeList.size() - 1; i > userTypeIndex; --i) {
				userTypeList.set(i, userTypeList.get(i - 1));
			}
			userTypeList.set(userTypeIndex, temp);
		}
		else {
			userTypeList.set(0, temp);
		}
		
	}
	
	// chk
	public void InsertItem(int item_idx,  String name,  String item) {
		int ilist_idx = _GetIlistIndex(ilist, item_idx, 1);

		ilist.add(0);
		for (int i = ilist_idx + 1; i < ilist.size(); ++i) {
			ilist.set(i, ilist.get(i - 1));
		}
		ilist.set(ilist_idx, 1);

		itemList.add(null);
		for (int i = item_idx + 1; i < itemList.size(); ++i) {
			itemList.set(i, itemList.get(i - 1));
		}
		itemList.set(item_idx, new ItemType(name, item));
	}
	// chk
	public void InsertUserType(int ut_idx,  UserType item) {
		int ilist_idx = _GetIlistIndex(ilist, ut_idx, 2);
		UserType temp = item;

		temp.parent = this;

		ilist.add(0);
		for (int i = ilist_idx + 1; i < ilist.size(); ++i) {
			ilist.set(i, ilist.get(i - 1));
		}
		ilist.set(ilist_idx, 2);

		userTypeList.add(null);
		for (int i = ut_idx + 1; i < userTypeList.size(); ++i) {
			userTypeList.set(i, userTypeList.get(i - 1));
		}
		userTypeList.set(ut_idx, temp);
	}
	
	//
	public void AddItem(String name, String item) {
		itemList.add(new ItemType(name, item));
		ilist.add(1);
	}
	public void AddUserTypeItem(UserType item) {
		UserType temp = item;
		temp.parent = this;

		ilist.add(2);

		userTypeList.add(temp);
	}
	
	public void AddItemAtFront(String name, String item) {
		itemList.add(0, new ItemType(name, item));

		ilist.add(0, 1);
	}
	
	public void AddUserTypeItemAtFront(UserType item) {
		UserType temp = item;
		temp.parent = this;

		ilist.add(0, 2);

		userTypeList.add(0, temp);
	}
	
	public ArrayList<ItemType> GetItem(String name)  {
		ArrayList<ItemType> temp = new ArrayList<ItemType>();
		
		if (name.startsWith("$.") && name.length() >= 5) {
			String regex = name.substring(3, name.length() - 1); // -1 . " , end?

			for (int i = 0; i < itemList.size(); ++i) {
				if (Pattern.matches(regex, itemList.get(i).GetName())) {
					temp.add(itemList.get(i));
				}
			}
		}
		else {
			for (int i = 0; i < itemList.size(); ++i) {
				if (itemList.get(i).GetName().equals(name)) {
					temp.add(itemList.get(i));
				}
			}
		}
		return temp;
	}
	// regex to SetItem?
	public boolean SetItem(String name, String value) throws Exception {
		int index = -1;

		for (int i = 0; i < itemList.size(); ++i) {
			if (itemList.get(i).GetName().equals(name))
			{
				this.GetItemList(i).Set(value);
				index = i;
			}
		}

		return -1 != index;
	}
	/// add set Data
	public boolean SetItem(int var_idx,  String value) throws Exception {
		itemList.get(var_idx).Set(value);
		return true;
	}
	
	public ArrayList<UserType> GetUserTypeItem(String name)  { /// chk...
		ArrayList<UserType> temp = new ArrayList<UserType>();

		for (int i = 0; i < userTypeList.size(); ++i) {
			if (userTypeList.get(i).GetName().equals(name)) {
				temp.add(userTypeList.get(i));
			}
		}

		return temp;
	}

	// deep copy.
	public ArrayList<UserType> GetCopyUserTypeItem(String name)  { /// chk...
		ArrayList<UserType> temp = new ArrayList<UserType>();

		for (int i = 0; i < userTypeList.size(); ++i) {
			if (userTypeList.get(i).GetName().equals(name)) {
				temp.add(new UserType(userTypeList.get(i)));
			}
		}

		return temp;
	}

	public boolean GetUserTypeItemRef(int idx, UserType ref)
	{
		ref = userTypeList.get(idx);
		return true;
	}
	public boolean GetLastUserTypeItemRef(String name, UserType ref) {
		int idx = -1;

		for (int i = userTypeList.size()-1; i >= 0; --i)
		{
			if (name.equals(userTypeList.get(i).GetName())) {
				idx = i;
				break;
			}
		}
		if (idx > -1) {
			ref = userTypeList.get(idx);
		}
		return idx > -1;
	}

	/// save1 - like EU4 savefiles.
	private void Save1(BufferedWriter stream, UserType ut) throws Exception
	{
		Save1(stream, ut, 0);
	}
	private void Save1(BufferedWriter stream, UserType ut,  int depth) throws Exception  {
		int itemListCount = 0;
		int userTypeListCount = 0;

		for (int i = 0; i < ut.commentList.size(); ++i) {
			for (int k = 0; k < depth; ++k) {
				stream.write("\t");
			}
			stream.write(ut.commentList.get(i));

			if (i < ut.commentList.size() - 1 || false == ut.ilist.isEmpty()) {
				stream.write("\n");
			}
		}

		for (int i = 0; i < ut.ilist.size(); ++i) {
			//cout << "ItemList" << endl;
			if (ut.ilist.get(i) == 1) {
				for (int j = 0; j < ut.itemList.get(itemListCount).size(); j++) {
					String temp="";
					for (int k = 0; k < depth; ++k) {
						temp += "\t";
					}
					if (ut.itemList.get(itemListCount).GetName().equals("") == false) {	
						temp += ut.itemList.get(itemListCount).GetName();
						temp += "=";
					}
					temp += ut.itemList.get(itemListCount).Get();
					if (j != ut.itemList.get(itemListCount).size() - 1) {
						temp += "\n";
					}
					stream.write(temp);
				}
				if (i != ut.ilist.size() - 1) {
					stream.write("\n");
				}
				itemListCount++;
			}
			else if (ut.ilist.get(i) == 2) {
				// cout << "UserTypeList" << endl;
				for (int k = 0; k < depth; ++k) {
					stream.write("\t");
				}

				if (ut.userTypeList.get(userTypeListCount).GetName().equals("") == false) {
					stream.write(ut.userTypeList.get(userTypeListCount).GetName() +  "=");
				}

				stream.write("{\n");
				
				Save1(stream, ut.userTypeList.get(userTypeListCount), depth + 1);
				stream.write("\n");
				
				for (int k = 0; k < depth; ++k) {
					stream.write("\t");
				}
				stream.write("}");
				if (i != ut.ilist.size() - 1) {
					stream.write("\n");
				}

				userTypeListCount++;
			}
		}
	}
	/// savw2 - for more seed loading data!
	private void Save2(BufferedWriter stream, UserType ut) throws Exception
	{
		Save2(stream, ut, 0);
	}
	private void Save2(BufferedWriter stream, UserType ut,  int depth) throws Exception  {
		int itemListCount = 0;
		int userTypeListCount = 0;

		for (int i = 0; i < ut.commentList.size(); ++i) {
			for (int k = 0; k < depth; ++k) {
				stream.write("\t");
			}
			stream.write (ut.commentList.get(i));

			if (i < ut.commentList.size() - 1 || false == ut.ilist.isEmpty()) {
				stream.write("\n");
			}

		}
		for (int i = 0; i < ut.ilist.size(); ++i) {
			//cout << "ItemList" << endl;
			if (ut.ilist.get(i) == 1) {
				for (int j = 0; j < ut.itemList.get(itemListCount).size(); j++) {
					for (int k = 0; k < depth; ++k) {
						stream.write("\t");
					}
					if (ut.itemList.get(itemListCount).GetName() != "")
						stream.write(ut.itemList.get(itemListCount).GetName() + " = ");
					stream.write(ut.itemList.get(itemListCount).Get());
					if (j != ut.itemList.get(itemListCount).size() - 1)
						stream.write(" ");
				}
				if (i != ut.ilist.size() - 1) {
					stream.write("\n");
				}
				itemListCount++;
			}
			else if (ut.ilist.get(i) == 2) {
				// cout << "UserTypeList" << endl;
				if (ut.userTypeList.get(userTypeListCount).GetName().equals("") == false)
				{
					stream.write(ut.userTypeList.get(userTypeListCount).GetName() + " = ");
				}
				stream.write("{\n");
				
				Save2(stream, ut.userTypeList.get(userTypeListCount), depth + 1);
				stream.write("\n");
				
				for (int k = 0; k < depth; ++k) {
					stream.write("\t");
				}
				stream.write("}");
				if (i != ut.ilist.size() - 1) {
					stream.write("\n");
				}
				userTypeListCount++;
			}
		}
	}

	public void Save1(BufferedWriter stream) throws Exception  {
		Save1(stream, this);
	}
	public void Save2(BufferedWriter stream) throws Exception  {
		Save2(stream, this);
	}
	public String ItemListToString() throws Exception
	{
		String temp="";
		int itemListCount = 0;

		for (int i = 0; i < itemList.size(); ++i) {
			for (int j = 0; j < itemList.get(itemListCount).size(); j++) {
				if (itemList.get(itemListCount).GetName().isEmpty() == false)
					temp = temp + itemList.get(itemListCount).GetName() + " = ";
				temp = temp + itemList.get(itemListCount).Get();
				if (j != itemList.get(itemListCount).size() - 1) {
					temp = temp + "/";
				}
			}
			if (i != itemList.size() - 1)
			{
				temp = temp + "/";
			}
			itemListCount++;
		}
		return temp;
	}
	public String ItemListNamesToString()
	{
		String temp="";
		int itemListCount = 0;

		for (int i = 0; i < itemList.size(); ++i) {
			for (int j = 0; j < itemList.get(itemListCount).size(); j++) {
				if (itemList.get(itemListCount).GetName().isEmpty() == false)
					temp = temp + itemList.get(itemListCount).GetName();
				else
					temp = temp + " ";

				if (j != itemList.get(itemListCount).size() - 1) {
					temp = temp + "/";
				}
			}
			if (i != itemList.size() - 1)
			{
				temp = temp + "/";
			}
			itemListCount++;
		}
		return temp;
	}
	public ArrayList<String> userTypeListNamesToStringArray()
	{
		ArrayList<String> temp = new ArrayList<String>();
		int userTypeListCount = 0;

		for (int i = 0; i < userTypeList.size(); ++i) {
			if (userTypeList.get(userTypeListCount).GetName().isEmpty() == false) {
				temp.add(userTypeList.get(userTypeListCount).GetName());
			}
			else {
				temp.add(" "); // chk!! cf) wiz::load_data::Utility::Find function...
			}
			userTypeListCount++;
		}
		return temp;
	}
	public String UserTypeListNamesToString()
	{
		String temp="";
		int userTypeListCount = 0;

		for (int i = 0; i < userTypeList.size(); ++i) {
			if (userTypeList.get(userTypeListCount).GetName().isEmpty() == false) {
				temp = temp + userTypeList.get(userTypeListCount).GetName();
			}
			else {
				temp = temp + " "; // chk!! cf) wiz::load_data::Utility::Find function...
			}

			if (i != itemList.size() - 1)
			{
				temp = temp + "/";
			}
			userTypeListCount++;
		}
		return temp;
	}
	public String ToString() throws Exception
	{
		String temp="";
		int itemListCount = 0;
		int userTypeListCount = 0;

		for (int i = 0; i < ilist.size(); ++i) {
			//cout << "ItemList" << endl;
			if (ilist.get(i) == 1) {
				for (int j = 0; j < itemList.get(itemListCount).size(); j++) {
					if (itemList.get(itemListCount).GetName() != "") {
						temp += (itemList.get(itemListCount).GetName());
						temp += (" = ");
					}
					temp += (itemList.get(itemListCount).Get());
					if (j != itemList.get(itemListCount).size() - 1)
					{
						temp += (" ");
					}
				}
				if (i != ilist.size() - 1) {
					temp += (" ");
				}
				itemListCount++;
			}
			else if (ilist.get(i) == 2) {
				// cout << "UserTypeList" << endl;
				if (userTypeList.get(userTypeListCount).GetName() != "") {
					temp += (userTypeList.get(userTypeListCount).GetName());
					temp += (" = ");
				}
				temp += ( " { ");
				temp += (userTypeList.get(userTypeListCount).ToString());
				temp += (" ");
				temp += (" }");
				if (i != ilist.size() - 1) {
					temp += (" ");
				}

				userTypeListCount++;
			}
		}
		return temp;
	}
	
	public static class Pair<First, Second>
	{
		public First first;
		public Second second;
		
		public Pair(First first, Second second)
		{
			this.first = first;
			this.second = second;
		}
		public Pair() { }
	}
	
	// find userType! not itemList!,// this has bug
	public static Pair<Boolean, ArrayList<UserType>> Find(UserType global, String _position) throws Exception /// option, option_offset
	{
		String position = _position;
		ArrayList< UserType > temp = new ArrayList<UserType>();

		if (!position.isEmpty() && position.charAt(0) == '@') { position = position.substring(1); }
		if (position.isEmpty()) { temp.add(global); return new Pair<Boolean, ArrayList<UserType>>(true, temp); }
		if (position == ".") { temp.add(global); return new Pair<Boolean, ArrayList<UserType>>(true, temp); }
		if (position == "/./") { temp.add(global); return new Pair<Boolean, ArrayList<UserType>>(true, temp); } // chk..
		if (position == "/.") { temp.add(global); return new Pair<Boolean, ArrayList<UserType>>(true, temp); }
		if (position.startsWith("/."))
		{
			position = position.substring(3);
		}

		ClauText.StringTokenizer tokenizer = new ClauText.StringTokenizer(position, "/");
		ArrayList<String> strVec = new ArrayList<String>();
		LinkedList<Pair<UserType, Integer>> utDeck = new LinkedList<Pair<UserType, Integer>>();
		Pair<UserType, Integer> utTemp = new Pair<UserType, Integer>();
		utTemp.first = global;
		utTemp.second = 0;

		for (int i = 0; i < tokenizer.countTokens(); ++i) {
			String strTemp = tokenizer.nextToken();
			if (strTemp == "root" && i == 0) {
			}
			else {
				strVec.add(strTemp);
			}

			if ((strVec.size() >= 1) && (strVec.get(strVec.size() - 1).equals(" "))) /// chk!!
			{
				strVec.set(strVec.size() - 1, "");
			}
		}

		// maybe, has bug!
		{
			int count = 0;

			for (int i = 0; i < strVec.size(); ++i) {
				if (strVec.get(i) == "..") {
					count++;
				}
				else {
					break;
				}
			}
				
			Collections.reverse(strVec);
			for (int i = 0; i < count; ++i) {
				if (utTemp.first == null) {
					return new Pair<Boolean, ArrayList<UserType>>( false, null );
				}
				utTemp.first = utTemp.first.GetParent();
				strVec.remove(strVec.size()-1);
			}
			Collections.reverse(strVec);
		}

		utDeck.addLast(utTemp);

		boolean exist = false;
		while (false == utDeck.isEmpty()) {
			utTemp = utDeck.getFirst();
			utDeck.removeFirst();
			
			if (utTemp.second < strVec.size() &&
					strVec.get(utTemp.second).startsWith("$ut")
				)
			{
				int idx = Integer.parseInt(strVec.get(utTemp.second).substring(3));

				if (idx < 0 || idx >= utTemp.first.GetUserTypeListSize()) {
					throw new Exception("ERROR NOT VALID IDX");
				}

				utDeck.addFirst(new Pair<UserType, Integer>(utTemp.first.GetUserTypeList(idx), utTemp.second + 1));
			}
			else if (utTemp.second < strVec.size() && strVec.get(utTemp.second).equals("$"))
			{
				for (int j = utTemp.first.GetUserTypeListSize() - 1; j >= 0; --j) {
					UserType x = utTemp.first.GetUserTypeList(j);
					utDeck.addFirst(new Pair<UserType, Integer>(x, utTemp.second + 1));
				}
			}
			else if (utTemp.second < strVec.size() && strVec.get(utTemp.second).startsWith("$.")) /// $."abc"
			{
				String rex_str = strVec.get(utTemp.second).substring(3, strVec.get(utTemp.second).length() - 1);

				for (int j = utTemp.first.GetUserTypeListSize() - 1; j >= 0; --j) {
					if (Pattern.matches(rex_str, utTemp.first.GetUserTypeList(j).GetName())) {
						UserType x = utTemp.first.GetUserTypeList(j);
						utDeck.addFirst(new Pair<UserType, Integer>(x, utTemp.second + 1));
					}
				}
			}
			else if (utTemp.second < strVec.size() &&
				(utTemp.first.GetUserTypeItem(strVec.get(utTemp.second)).isEmpty() == false))
			{
				ArrayList<UserType>  x = utTemp.first.GetUserTypeItem(strVec.get(utTemp.second));
				for (int j = x.size() - 1; j >= 0; --j) {
					utDeck.addFirst(new Pair<UserType, Integer>(x.get(j), utTemp.second + 1));
				}
			}

			if (utTemp.second == strVec.size()) {
				exist = true;
				temp.add(utTemp.first);
			}
		}
		if (false == exist) { return new Pair<Boolean, ArrayList<UserType>>(false, null); }
		return new Pair<Boolean, ArrayList<UserType>>(true, temp);
	}
}
