package gui.listeners;

/*
Interface usada para simular um evento de atualização da tabela Department.
Deverá ser implementada por qualquer classe que precisar ser atualizada quando ocorrer alguma atualização na referiada tabela.
No momento da atualização será chamado o método onDataChanged que deverá estar implementado na classe que precisa ser atualizada
*/
public interface DataChangeListener {
	
	void onDataChanged();

}
