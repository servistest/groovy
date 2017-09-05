
The script for merging two json with logical fields.

 Usage: groovy mergeLF.groovy [lf] [rule] [baself] [outdir]

 lf:  input logic field json
 rule: path to ruledir for lf json
 base: the logic fields as base, not change json
 outdir: the merged result

 eg : groovy ./mergeLF.groovy  FXIR/DIRChainlogicalfield.json   FXIR/ruleChain.json FXIR/DIRQuotelogicalfield.json  FXIR/output")