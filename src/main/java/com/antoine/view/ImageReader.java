package com.antoine.view;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;


/**
 * <p>Classe de service pour créer des BufferedImage à partir de son path.</p>
 *
 * @author antoine
 */
public class ImageReader {

    /**
     * <p>Importe le fichier sous forme de BufferedImage.</p>
     * @param imageUrl url du fichier
     * @return une BufferedImage
     * @throws RuntimeException si erreur à la lecture du fichier
     */
    public static BufferedImage readImage(String imageUrl) {
        try {
            return ImageIO.read(ImageReader.class.getResourceAsStream(imageUrl));
        }catch (IOException ioe){
            throw new RuntimeException("Erreur de lecture de l'image");
        }
    }
}
