package uni.tubingen.inference.featureextractor;

import org.knime.core.node.NodeView;



/**
 * <code>NodeView</code> for the "FeatureExtractor" Node.
 * 
 *
 * @author enrique
 */
public class FeatureExtractorNodeView extends NodeView<FeatureExtractorNodeModel> {

    /**
     * Creates a new view.
     * 
     * @param nodeModel The model (class: {@link FeatureExtractorNodeModel})
     */
    protected FeatureExtractorNodeView(final FeatureExtractorNodeModel nodeModel) {
        super(nodeModel);
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void modelChanged() {
    	FeatureExtractorNodeModel nodeModel = 
                getNodeModel();
            assert nodeModel != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onClose() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onOpen() {
        // TODO: generated method stub
    }

}

