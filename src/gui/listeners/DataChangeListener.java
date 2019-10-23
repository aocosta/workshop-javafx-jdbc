package gui.listeners;

/*
Interface usada para simular um evento de atualiza��o da tabela Department.
Dever� ser implementada por qualquer classe que precisar ser atualizada quando ocorrer alguma atualiza��o na referiada tabela.
No momento da atualiza��o ser� chamado o m�todo onDataChanged que dever� estar implementado na classe que precisa ser atualizada
*/
public interface DataChangeListener {
	
	void onDataChanged();

}
