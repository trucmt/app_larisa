# larisa
Larisa mobile app
Features: 
  Real time scanning
  On device inference
  Lightweight
  100% free and no ads
Larisa uses permissions and intent-filters to detect malicious apps. While scanning, it loads the machine learning model and extracts permissions and intents from the installed applications on the user's device. These extracted features are then fed to the machine learning model in the form of a vector. The machine learning model returns a prediction score between 0 and 1 that denote the degree of maliciousness of the scanned application. We use this score to classify the scanned app into one of the following categories:

  Goodware: The prediction score is less than 0.5
  Risky: Prediction score between 0.5 and 0.75
  Malware: Prediction score is greater than 0.75
  Unknown: If LibreAV is unable to extract permissions and intents from an app, then that app is labelled as 'Unknown'
