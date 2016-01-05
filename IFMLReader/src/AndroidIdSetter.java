import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.EventObject;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.EventListenerList;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.TableView;

public class AndroidIdSetter {
	private ArrayList<ViewElement> list = null;
	private final static Class[] visiualElementType = {ViewContainer.class, ViewComponent.class, Menu.class};
	public AndroidIdSetter(ArrayList<ViewElement> list) {
		this.setList(list);
	}
	public ArrayList<ViewElement> getList() {
		return list;
	}
	public void setList(ArrayList<ViewElement> list) {
		this.list = list;
	}
	
	public void initSetter() {
		Toolkit kit=Toolkit.getDefaultToolkit();
		Dimension screensize=kit.getScreenSize();
		int screenheight=screensize.height;
		int screenwidth=screensize.width;

		JFrame frame = new JFrame();
		frame.setSize(screenwidth/2,screenheight/2);
		frame.setLocation(screenwidth/4,screenheight/4); 
		frame.setVisible(true);
		frame.setTitle("IdSetter");
		frame.setLayout(null);
		
		//panel为主面板
		JPanel panel = new JPanel();
		panel.setLocation(40, 30);
		panel.setSize(screenwidth/2 - 100, screenheight/2 - 100);
		panel.setVisible(true);
		panel.setLayout(null);
		//panel.setBackground(Color.red);
		frame.add(panel);
		
		//下面为将数据绑定到table中
		  
		SetterTableModel myModel = new SetterTableModel(list);
		JTable table = new JTable(myModel);
		table.setRowHeight(25);
		TableColumnModel tcm= table.getColumnModel();
        TableColumn tc = tcm.getColumn(3);
        //设置“性别”列的单元格渲染器（renderer）
        tc.setCellRenderer(new AndroidTypeRenderer());
        tc.setCellEditor(new AndroidTypeEditor());
		//scorePanel用来存放带滚动条的table
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setVisible(true);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.setSize(screenwidth/2 - 100, screenheight/2 - 150);
		panel.add(scrollPane);
		
		 

	}
	
	private class SetterTableModel  extends AbstractTableModel{
		/**
		 * 
		 */
		private static final long serialVersionUID = 7679117804117813045L;
		private String[] headName = { "ViewElementType", "Name", "ParentContainer", "AndroidType", "Id"};
		private Object[][] values = null;
		
		public SetterTableModel(ArrayList<ViewElement> list) {
			int columnLength = 0;
			if (null != list) {
				for (ViewElement e : list) {
					for (Class visiableClass : visiualElementType) {
						if (e.getClass().isAssignableFrom(visiableClass)) {
							columnLength ++;
							break;
						}
					}
				}
				values = new Object[columnLength][5];
				int index = 0;
				for (ViewElement e : list) {
					for (Class visiableClass : visiualElementType) {
						if (e.getClass().isAssignableFrom(visiableClass)) {
							//这里暂时使用了class名来作为ViewElementType，后面随着其增多可以自行创建一个字段
							values[index][0] = e.getClass().toString().substring(6);
							values[index][1] = e.getName();
							ViewContainer directParent = e.getDirectParentContainer();
							values[index][2] = (null != directParent) ? directParent.getName() : "";
							values[index][3] = "";
							values[index][4] = e.getId();
							index++;
							break;
						}
					}
				}
			}
		}

		@Override
		public int getColumnCount() {
			// TODO Auto-generated method stub
			return values[0].length;
		}

		@Override
		public int getRowCount() {
			// TODO Auto-generated method stub
			return values.length;
		}

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			// TODO Auto-generated method stub
			return values[rowIndex][columnIndex];
		}
		
        public String getColumnName(int column){
            return headName[column];
        }
        
        public boolean isCellEditable(int row, int column){
        	if ((column == 0) || (column == 1) || (column ==2) ) {
        		return false;
        	}
            return true;
        }
        
        public void setValueAt(Object value, int row, int column){
            values[row][column]= value;
        }
	}
	
	public class AndroidTypeRenderer extends JComboBox implements TableCellRenderer{
        private static final long serialVersionUID = -8624401777277852691L;
        public AndroidTypeRenderer(){
                  super();
                  AndroidType[] list = AndroidType.values();
                  for (AndroidType type : list) {
                	  addItem(type);
                  }
        }
        public Component getTableCellRendererComponent(JTable table, Object value,
                           boolean isSelected, boolean hasFocus, int row, int column) {
                  if(isSelected){
                           setForeground(table.getForeground());
                           super.setBackground(table.getBackground());
                  }else{
                           setForeground(table.getForeground());
                           setBackground(table.getBackground());
                  }
                  if ((null == value) || value.equals("")) {
                  	//设为NULL
                  	setSelectedIndex(0);
                  } else if (value.getClass() == AndroidType.class) {
                  	for (AndroidType type : AndroidType.values()) {
                  		if (type == value) {
                  			setSelectedIndex(type.ordinal());
                  		}
                  	}
                  }
                  return this;
        }

	}
	
	public class AndroidTypeEditor extends JComboBox implements TableCellEditor{
	       
        private static final long serialVersionUID = 5860619160549087886L;
        private final AndroidType[] androidType = AndroidType.values();
        //EventListenerList:保存EventListener 列表的类。
        private EventListenerList listenerList = new EventListenerList();
        //ChangeEvent用于通知感兴趣的参与者事件源中的状态已发生更改。
        private ChangeEvent changeEvent = new ChangeEvent(this);
        public AndroidTypeEditor(){
        		super();
                for (AndroidType type : androidType) {
              	  addItem(type);
                }
                  //请求终止编辑操作可以包含单元格的JTable收到，也可以从编辑器组件本身（如这里的JComboBox）获得
                  /*addActionListener(newActionListener(){
                           publicvoid actionPerformed(ActionEvent e) {
                                    System.out.println("ActionListener");
                                    //如同stopCellEditing，都是调用fireEditingStopped()方法
                                    fireEditingStopped();
                           }
                         
                  });*/
        }
        public void addCellEditorListener(CellEditorListener l) {
                  listenerList.add(CellEditorListener.class,l);
        }
        public void removeCellEditorListener(CellEditorListener l) {
                  listenerList.remove(CellEditorListener.class,l);
        }
        private void fireEditingStopped(){
                  CellEditorListener listener;
                  Object[]listeners = listenerList.getListenerList();
                  for(int i = 0; i < listeners.length; i++){
                           if(listeners[i]== CellEditorListener.class){
                                    //之所以是i+1，是因为一个为CellEditorListener.class（Class对象），
                                    //接着的是一个CellEditorListener的实例
                                    listener= (CellEditorListener)listeners[i+1];
                                    //让changeEvent去通知编辑器已经结束编辑
                                    //在editingStopped方法中，JTable调用getCellEditorValue()取回单元格的值，
                                    //并且把这个值传递给TableValues(TableModel)的setValueAt()
                                    listener.editingStopped(changeEvent);
                           }
                  }
        }
        public void cancelCellEditing() {        
        }
        /**
         * 编辑其中一个单元格，再点击另一个单元格时，调用。-------------！！！！！
         */
        public boolean stopCellEditing() {
                  //可以注释掉下面的fireEditingStopped();，然后在GenderEditor的构造函数中把
                  //addActionListener()的注释去掉（这时请求终止编辑操作从JComboBox获得），
                  
                  fireEditingStopped();//请求终止编辑操作从JTable获得
                  return true;
        }
        /**
         * 为一个单元格初始化编辑时，getTableCellEditorComponent被调用
         */
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            if ((null == value) || value.equals("")) {
            	//设为NULL
            	setSelectedIndex(0);
            } else if (value.getClass() == AndroidType.class) {
            	for (AndroidType type : androidType) {
            		if (type == value) {
            			setSelectedIndex(type.ordinal());
            		}
            	}
            }
        	return this;
        }
        /**
         * 询问编辑器它是否可以使用 anEvent 开始进行编辑。
         */
        public boolean isCellEditable(EventObject anEvent) {
            return true;
        }
        /**
         * 如果应该选择正编辑的单元格，则返回true，否则返回 false。
         */
        public boolean shouldSelectCell(EventObject anEvent) {
            return true;
        }

        /**
         * 返回值传递给TableValue（TableModel）中的setValueAt()方法
         */
        public Object getCellEditorValue() {
        	
            return androidType[getSelectedIndex()];
        }
	} 
	
	public static void main(String[] arg) {
//		AndroidIdSetter a = new AndroidIdSetter(null);
//		a.initSetter();
		if(ViewContainer.class.isAssignableFrom(Menu.class)) {
			System.out.println("OK");
		}
	}
}
