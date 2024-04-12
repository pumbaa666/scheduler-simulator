package ch.correvon.scheduler.moo.myObjects;

import javax.swing.DefaultListModel;
import javax.swing.JList;

public class MyJList extends JList 
{
	/*------------------------------------------------------------------------------*\
	|*								Constructeur									*|
	\*------------------------------------------------------------------------------*/
	public MyJList(DefaultListModel model)
	{
		super(model);
	}
	
	/*------------------------------------------------------------------------------*\
	|*							Methodes publiques									*|
	\*------------------------------------------------------------------------------*/
	public void up()
	{
		int indices[] = this.getSelectedIndices();
		int index1;
		int index2;
		for(int i = 0; i < indices.length ; i++)
		{
			index1 = indices[i];
			index2 = index1 - 1;
			if(index1 > 0)
				this.switchProcess(index1, index2);
		}
		this.increaseTabIndices(indices);
		this.setSelectedIndices(indices);
	}
	
	public void down()
	{
		int tabIndices[] = this.getSelectedIndices();
		int index1;
		int index2;
		for(int i = tabIndices.length-1; i >= 0 ; i--)
		{
			index1 = tabIndices[i];
			index2 = index1 + 1;
			if(index1 != -1 && index1 < super.getModel().getSize()-1)
				this.switchProcess(index1, index2);
		}
		this.decreaseTabIndice(tabIndices);
		this.setSelectedIndices(tabIndices);
	}
	
	public void upTop()
	{
		int tabIndices[] = this.getSelectedIndices();
		for(int j = 0; j < tabIndices.length ; j++)
			for(int i = tabIndices[j]; i > 0; i--)
			{
				this.setSelectedIndex(i);
				this.up();
			}
		
		// Permute les process
		for(int i = 0; i < tabIndices.length/2; i++)
			switchProcess(i, tabIndices.length-i-1);
		
		// Nouvelle sélection
		for(int i = 0; i < tabIndices.length; i++)
			tabIndices[i] = tabIndices.length-i-1;
		this.setSelectedIndices(tabIndices);
	}
	
	public void downBottom()
	{
		int tabIndices[] = this.getSelectedIndices();
		int listBoxSize = this.getModel().getSize();
		for(int j = tabIndices.length-1; j >= 0 ; j--)
			for(int i = tabIndices[j]; i < this.getModel().getSize()-1; i++)
			{
				this.setSelectedIndex(i);
				this.down();
			}
		
		// Permute les process
		for(int i = 0; i < tabIndices.length/2; i++)
			this.switchProcess(listBoxSize-1-i, listBoxSize-tabIndices.length+i);
		
		// Nouvelle sélection
		for(int i = 0; i < tabIndices.length; i++)
			tabIndices[i] = listBoxSize-i-1;
		this.setSelectedIndices(tabIndices);
	}
	
	public void switchElements()
	{
		int tabIndices[] = this.getSelectedIndices();
		
		if(tabIndices.length > 1)
			for(int i = tabIndices.length-1; i > 0 ; i--)
				this.switchProcess(tabIndices[i], tabIndices[i-1]);
		
		this.setSelectedIndices(tabIndices);
	}

	
	
	public void remove(int[] tab)
	{
		DefaultListModel model = (DefaultListModel)super.getModel();
		for(int i = 0; i < tab.length; i++)
			model.removeElementAt(tab[i]-i);
	}
	
	/**
	 * Echange de place 2 processus dans la liste des processus ( d'entrée )
	 */
	private void switchProcess(int index1, int index2)
	{
		DefaultListModel model = (DefaultListModel)super.getModel();
		Object temp = model.get(index1);
		model.set(index1, model.get(index2));
		model.set(index2, temp);
	}
	
	private void decreaseTabIndice(int[] tab)
	{
		for(int i = 0; i < tab.length; i++)
			tab[i]++;
	}
	
	private void increaseTabIndices(int[] tab)
	{
		for(int i = 0; i < tab.length; i++)
			tab[i]--;
	}
}
