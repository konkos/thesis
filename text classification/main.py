import json
from json import JSONEncoder

import numpy as np
from flask import Flask, request
from transformers import pipeline, AutoTokenizer

app = Flask(__name__)

model_name = "dbmdz/electra-large-discriminator-finetuned-conll03-english"
ner_pipeline = pipeline("ner", model=model_name, tokenizer=AutoTokenizer.from_pretrained(
    "dbmdz/electra-large-discriminator-finetuned-conll03-english")
                        )


class CustomEncoder(JSONEncoder):
    def default(self, obj):
        if isinstance(obj, np.float32):
            return float(obj)
        return JSONEncoder.default(self, obj)


@app.route('/', methods=['POST'])
def categorize():
    text = request.json['input']
    print(f"text => {text}")
    keywords = ner_pipeline(text)
    print(f"KWDS = {keywords}")

    # print_dict_types(m_dict)

    return json.dumps(keywords, cls=CustomEncoder)


if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5000)