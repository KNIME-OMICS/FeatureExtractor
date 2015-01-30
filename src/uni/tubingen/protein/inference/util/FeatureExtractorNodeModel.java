package uni.tubingen.protein.inference.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

import org.knime.core.data.DataCell;
import org.knime.core.data.DataColumnSpec;
import org.knime.core.data.DataColumnSpecCreator;
import org.knime.core.data.DataRow;
import org.knime.core.data.DataTableSpec;
import org.knime.core.data.RowIterator;
import org.knime.core.data.RowKey;
import org.knime.core.data.StringValue;
import org.knime.core.data.def.DefaultRow;
import org.knime.core.data.def.StringCell;
import org.knime.core.node.BufferedDataContainer;
import org.knime.core.node.BufferedDataTable;
import org.knime.core.node.CanceledExecutionException;
import org.knime.core.node.ExecutionContext;
import org.knime.core.node.ExecutionMonitor;
import org.knime.core.node.InvalidSettingsException;
import org.knime.core.node.NodeModel;
import org.knime.core.node.NodeSettingsRO;
import org.knime.core.node.NodeSettingsWO;

import org.apache.commons.lang3.*;



/**
 * This is the model implementation of FeatureExtractor.
 * 
 *
 * @author enrique
 */
public class FeatureExtractorNodeModel extends NodeModel {
	
	//field
	static BufferedDataContainer container = null;
	
	private static HashMap <String, String> peptide2detectability = new HashMap <String, String> (100000);
    
    /**
     * Constructor for the node model.
     */
    protected FeatureExtractorNodeModel() {
    
        // TODO: Specify the amount of input and output ports needed.
        super(1, 1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected BufferedDataTable[] execute(final BufferedDataTable[] inData,
            final ExecutionContext exec) throws Exception {
    	
    	DataTableSpec new_spec_table = new DataTableSpec(make_output_spec());  	
        container = exec.createDataContainer(new_spec_table);

    	 try{
 	    	
    		 //load the peptide sequences;
    	RowIterator row_it = inData[0].iterator();
    	
    	System.out.println(inData[0].getSpec().getNumColumns());
    	
    	String sequence = null;
		String detectability = null;
    	
    	while (row_it.hasNext()) {
    		
     		//getting information from current row...
    		DataRow r = row_it.next();
    		DataCell current_cell = r.getCell(inData[0].getSpec().getNumColumns()-1);
    		
    		
    		// rows with missing cells cannot be processed (no missing values in PSM graph...)
    		   if (current_cell.isMissing()) {
    			  continue;
    		   }
    		//procces current table row
    		   String xml_line_entry   =   ((StringValue) current_cell).getStringValue();
    		  
    		   if (xml_line_entry.contains("sequence")){
    			   StringTokenizer token = new StringTokenizer (xml_line_entry);
    			     while(token.hasMoreElements()){
    			    	 String temp = token.nextToken();
    			    	 if (temp.contains("sequence")){
    			    		 sequence = StringUtils.substring(temp, 10, temp.length()-1);
    			    	 }
    			     }			   
    		    }
    		   
    		   if (xml_line_entry.contains("predicted_PT")){
    			   StringTokenizer token = new StringTokenizer (xml_line_entry);
    			     while(token.hasMoreElements()){
    			    	 String temp = token.nextToken();
    			    	 if (temp.contains("value")){
    			    		 detectability = StringUtils.substring(temp, 7, temp.length()-3);
    			    	 }
    			     }	
    			     
    			   //fill map peptide to detectability table
    			     peptide2detectability.put(sequence, detectability);
    		    }		   
    		
    	  }
    	
    	 }catch (Exception x){
    	x.printStackTrace(System.err);
        }
    	
    	//fill container 
    	if (!peptide2detectability.isEmpty()){
    	  Set<String> peptides = peptide2detectability.keySet();
    	     for (String peptide : peptides){
    	    	    RowKey key = new RowKey(String.valueOf(new Integer(peptide.hashCode())));
    	            DataCell[] cells = new DataCell[2];	    		
    	     		cells[0] = new StringCell(peptide);
    	     		cells[1] = new StringCell(peptide2detectability.get(peptide));
    	     		DataRow row = new DefaultRow(key, cells);
    	     		container.addRowToTable(row);
    	     }
    		
    	}
    
    	  container.close(); 
          return new BufferedDataTable[]{ container.getTable() };
    }
    
    

    private DataColumnSpec[]  make_output_spec() {   	
    	DataColumnSpec cols[] = new DataColumnSpec[2];
    	cols[0] = new DataColumnSpecCreator("Peptide", StringCell.TYPE).createSpec();
    	cols[1] = new DataColumnSpecCreator("Detectability", StringCell.TYPE).createSpec();	
      return cols;
	}

    /**
     * {@inheritDoc}
     */
    @Override
    protected void reset() {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataTableSpec[] configure(final DataTableSpec[] inSpecs)
            throws InvalidSettingsException {

        // TODO: generated method stub
        return new DataTableSpec[]{null};
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveSettingsTo(final NodeSettingsWO settings) {
         // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadValidatedSettingsFrom(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        // TODO: generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void validateSettings(final NodeSettingsRO settings)
            throws InvalidSettingsException {
        // TODO: generated method stub
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void loadInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    protected void saveInternals(final File internDir,
            final ExecutionMonitor exec) throws IOException,
            CanceledExecutionException {
        // TODO: generated method stub
    }

}

