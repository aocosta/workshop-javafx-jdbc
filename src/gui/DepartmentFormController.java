package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.exceptions.ValidationException;
import model.services.DepartmentService;

public class DepartmentFormController implements Initializable {
	
	private Department entity;
	
	private DepartmentService service;
	
	// Lista que conterá os objetos que serão atualizados quando ocorrer alguma altualização na tabela Department
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtName;
	
	@FXML
	private Label labelErrorName;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	// Inclui um objeto na lista de objetos que serão atualizados quando ocorrer alguma atualização na tabela Department
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	@FXML
	private void onBtSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}
		try {
			// Popula a entidade com os dados do formulário
			entity = getFormData();
			
			// salva no banco
			service.saveOrUpdate(entity);
			
			// Atualiza as informações da DepartmentListController
			// Executa o método onDataChanged dos objetos que implementaram a interface DataChangeListener
			notifyDataChangeListener();
			
			// Fecha a janela
			Utils.currentStage(event).close();
		}
		// Exceção lançada pelo método getFormData
		// Exceção para validar os campos do formulário de departamento
		catch (ValidationException e) {
			// Apresenta as mensagens de erro
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error saving object", null, e.getMessage(), AlertType.ERROR);
		}
	}

	// Atualiza todos os objetos precisam ser atualizados devido a atualização da tabela Department
	private void notifyDataChangeListener() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDataChanged();
		}
	}

	// Retorna a entidade com os dados do formulário
	private Department getFormData() {
		// Instancia a entidade
		Department obj = new Department();
		// Cria uma exceção para erros no preenchimento do formulário
		ValidationException exception = new ValidationException("Validation Error");

		// Seta o id com nulo ou um inteiro
		obj.setId(Utils.tryParseToInt(txtId.getText()));
		
		// Valida o nome digitado no formulário
		if (txtName.getText() == null || txtName.getText().trim().equals("")) {
			// Adiciona mensagem de error na exceção
			exception.addError("name", "Field can't be empty");
		}
		
		// Seta o nome com o valor digitado no formulário
		obj.setName(txtName.getText());
		
		// Se existe erro de validação, propaga a exceção
		if (exception.getErrors().size() > 0) {
			throw exception;
		}
		
		return obj;
	}

	@FXML
	private void onBtCancelAction(ActionEvent event) {
		// Fecha a janela
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldMaxLength(txtName, 30);
	}
	
	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtName.setText(entity.getName());
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("name")) {
			labelErrorName.setText(errors.get("name"));
		}
	}

}
