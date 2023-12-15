from flask import Flask, request
from transformers import pipeline

app = Flask(__name__)

model_name = "dbmdz/electra-large-discriminator-finetuned-conll03-english"
ner_pipeline = pipeline("ner", model=model_name, tokenizer=model_name)


@app.route('/', methods=['POST'])
def categorize():
    text = request.json['input']
    keywords = ner_pipeline(text)
    return keywords


if __name__=='__main__':
    app.run(host='0.0.0.0', port=5000)