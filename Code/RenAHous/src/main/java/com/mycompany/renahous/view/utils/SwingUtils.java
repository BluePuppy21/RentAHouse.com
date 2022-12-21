package com.mycompany.renahous.view.utils;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class SwingUtils {
	/**
	 * It will load the {@link JComponent} with the provided entities' information.
	 * It uses the {@link Object#toString()} method to obtain its {@link String} version as a representation in the component.
	 * The provided {@link Component} must be either a {@link JList} or a {@link JComboBox}, if the provided {@link JComponent} is not one of the supported
	 * {@link JComponent}s this method will throw an {@link IllegalArgumentException}.
	 *
	 * @param componentWithModel {@link Component} with a {@link ListModel} or implementing the interface.
	 * @param entities			{@link Collection} of elements tha represent the information you want to show in your {@link Component}.
	 * @param clazz				{@link Class} to specify the type of your entities.
	 * @param <T>				This generic will be automatically adjusted to your entities type.
	 * @throws IllegalArgumentException If the provided {@link JComponent} is not one of the supported {@link JComponent}s.
	 */
	@SuppressWarnings("unchecked")
	public static <T> void loadComponentWith(Component componentWithModel, Collection<T> entities, Class<T> clazz) {
		String logID="::loadComponentWith([componentWithModel, entities, clazz]): ";
		log.trace("{}Start ", logID);
		Objects.requireNonNull(entities);
		Objects.requireNonNull(componentWithModel);
		try {
			
			if (componentWithModel instanceof JComboBox) {
				ComboBoxModel<T> model = new DefaultComboBoxModel<>((T[]) entities.toArray());
				((JComboBox<T>) componentWithModel).setModel(model);
			} else if (componentWithModel instanceof JList) {
				DefaultListModel<T> model = new DefaultListModel<>();
				entities.forEach(model::addElement);
				model.addElement(entities.iterator().next());
//				model.addAll(entities); //java 12 and up
				((JList<T>) componentWithModel).setModel(model);
			} else throw new IllegalArgumentException(" The provided component must be a JComboBox or a JList");
			
			log.trace("{}Finish", logID);
			
		} catch (Exception e) {
			throw new RuntimeException("Impossible to load the filter " + componentWithModel + " with " + entities, e);
		}
	}
	
	public static Optional<ImageIcon> getImageIconFromJFileChooser(){
		return getImageIconFromJFileChooser(null);
	}
	
	@SneakyThrows
	public static Optional<ImageIcon> getImageIconFromJFileChooser(JComponent parent){
		String logID="::getImageIconFromJFileChooser([parent]): ";
		log.trace("{}Start ", logID);
		JFileChooser fileChooser = new JFileChooser();
		fileChooser.setDialogTitle("Please choose an image...");
		FileNameExtensionFilter filter = new FileNameExtensionFilter("JPEG", "jpeg", "jpg", "png", "bmp", "gif");
		fileChooser.addChoosableFileFilter(filter);
		
		return fileChooser.showOpenDialog(parent) == JFileChooser.APPROVE_OPTION?
				Optional.of(new ImageIcon(
						ImageIO.read(fileChooser.getSelectedFile())
				))
				: Optional.empty();
	}
	
	
	public static void loadImageFromJFileChooser(JLabel jLabel){
		loadImageFromJFileChooser(jLabel, null);
	}
	
	public static void loadImageFromJFileChooser(JLabel jLabel, JComponent parent){
		getImageIconFromJFileChooser(parent).ifPresent(jLabel::setIcon);
	}
	
}